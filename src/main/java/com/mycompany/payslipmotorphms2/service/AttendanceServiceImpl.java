package com.mycompany.payslipmotorphms2.service;

// Import necessary classes and packages
import com.mycompany.payslipmotorphms2.dao.CSVDataLoader;
import com.mycompany.payslipmotorphms2.model.Employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

// Implementation of the AttendanceService interface
public class AttendanceServiceImpl implements AttendanceService {
    // Fields to store employee data, work hours, and data loader
    private final List<Employee> employees;
    private final List<String[]> workHours;
    private final CSVDataLoader dataLoader;

    // Constructor to initialize the fields
    public AttendanceServiceImpl(List<Employee> employees, List<String[]> workHours, CSVDataLoader dataLoader) {
        this.employees = employees;
        this.workHours = workHours;
        this.dataLoader = dataLoader;
    }

    // Method to find an employee by their ID
    private Employee findEmployeeById(String employeeId) {
        return employees.stream()
                .filter(employee -> employee.getId().trim().equalsIgnoreCase(employeeId.trim()))
                .findFirst()
                .orElse(null);
    }

    // Method to find employees supervised by a given supervisor
    private List<Employee> findEmployeesBySupervisor(String supervisorName) {
        return employees.stream()
                .filter(employee -> employee.getSupervisor().trim().equalsIgnoreCase(supervisorName.trim()))
                .collect(Collectors.toList());
    }

    // Method to calculate total hours worked
    @Override
    public double calculateTotalHoursWorked(String employeeId, LocalDate startOfWeek, LocalDate endOfWeek) {
        // Calculate late hours, undertime hours, and overtime hours
        double lateHours = calculateLateHours(employeeId, startOfWeek, endOfWeek);
        double undertimeHours = calculateUndertimeHours(employeeId, startOfWeek, endOfWeek);
        double overtimeHours = calculateOvertimeHours(employeeId, startOfWeek, endOfWeek);

        // Compute total hours worked based on the formula: 40 - late - undertime + overtime
        double totalHoursWorked = 40 - lateHours - undertimeHours + overtimeHours;

        // Ensure total hours worked is not negative
        return Math.max(totalHoursWorked, 0);
    }

    // Method to calculate late hours
    @Override
    public double calculateLateHours(String employeeId, LocalDate startOfWeek, LocalDate endOfWeek) {
        LocalTime shiftStartTime = LocalTime.of(8, 0); // 8:00 AM
        LocalTime gracePeriodEnd = shiftStartTime.plusMinutes(10); // 8:10 AM

        return workHours.stream()
                .filter(record -> record[0].trim().equals(employeeId.trim()))
                .filter(record -> {
                    try {
                        LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                        return !logDate.isBefore(startOfWeek) && !logDate.isAfter(endOfWeek);
                    } catch (DateTimeParseException e) {
                        return false; // Skip invalid dates
                    }
                })
                .mapToDouble(record -> {
                    try {
                        String[] logInParts = record[3].trim().split(":");
                        LocalTime logInTime = LocalTime.of(Integer.parseInt(logInParts[0]), Integer.parseInt(logInParts[1]));
                        if (logInTime.isAfter(gracePeriodEnd)) {
                            return (logInTime.toSecondOfDay() - shiftStartTime.toSecondOfDay()) / 3600.0; // Convert seconds to hours
                        }
                        return 0.0;
                    } catch (Exception e) {
                        return 0.0; // Skip invalid time entries
                    }
                })
                .sum();
    }

    // Method to calculate undertime hours
    @Override
    public double calculateUndertimeHours(String employeeId, LocalDate startOfWeek, LocalDate endOfWeek) {
        LocalTime shiftEndTime = LocalTime.of(17, 0); // 5:00 PM

        return workHours.stream()
                .filter(record -> record[0].trim().equals(employeeId.trim()))
                .filter(record -> {
                    try {
                        LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                        return !logDate.isBefore(startOfWeek) && !logDate.isAfter(endOfWeek);
                    } catch (DateTimeParseException e) {
                        return false; // Skip invalid dates
                    }
                })
                .mapToDouble(record -> {
                    try {
                        String[] logOutParts = record[4].trim().split(":");
                        LocalTime logOutTime = LocalTime.of(Integer.parseInt(logOutParts[0]), Integer.parseInt(logOutParts[1]));
                        if (logOutTime.isBefore(shiftEndTime)) {
                            return (shiftEndTime.toSecondOfDay() - logOutTime.toSecondOfDay()) / 3600.0; // Convert seconds to hours
                        }
                        return 0.0;
                    } catch (Exception e) {
                        return 0.0; // Skip invalid time entries
                    }
                })
                .sum();
    }

    // Method to calculate overtime hours
    @Override
    public double calculateOvertimeHours(String employeeId, LocalDate startOfWeek, LocalDate endOfWeek) {
        LocalTime shiftStartTime = LocalTime.of(8, 0); // 8:00 AM
        LocalTime shiftEndTime = LocalTime.of(17, 0); // 5:00 PM

        return workHours.stream()
                .filter(record -> record[0].trim().equals(employeeId.trim()))
                .filter(record -> {
                    try {
                        LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                        return !logDate.isBefore(startOfWeek) && !logDate.isAfter(endOfWeek);
                    } catch (DateTimeParseException e) {
                        return false; // Skip invalid dates
                    }
                })
                .mapToDouble(record -> {
                    try {
                        String[] logInParts = record[3].trim().split(":");
                        String[] logOutParts = record[4].trim().split(":");
                        LocalTime logInTime = LocalTime.of(Integer.parseInt(logInParts[0]), Integer.parseInt(logInParts[1]));
                        LocalTime logOutTime = LocalTime.of(Integer.parseInt(logOutParts[0]), Integer.parseInt(logOutParts[1]));

                        double overtime = 0.0;
                        if (logInTime.isBefore(shiftStartTime)) {
                            overtime += (shiftStartTime.toSecondOfDay() - logInTime.toSecondOfDay()) / 3600.0; // Before shift start
                        }
                        if (logOutTime.isAfter(shiftEndTime)) {
                            overtime += (logOutTime.toSecondOfDay() - shiftEndTime.toSecondOfDay()) / 3600.0; // After shift end
                        }
                        return overtime;
                    } catch (Exception e) {
                        return 0.0; // Skip invalid time entries
                    }
                })
                .sum();
    }

    // Method to generate an individual attendance report
    @Override
    public String generateIndividualAttendanceReport(String employeeId, LocalDate date) {
        Employee employee = findEmployeeById(employeeId);
        if (employee == null) {
            return "Employee not found.";
        }

        // Calculate the start and end dates of the week
        LocalDate startOfWeek = date.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Calculate attendance details
        double totalHoursWorked = calculateTotalHoursWorked(employeeId, startOfWeek, endOfWeek);
        double totalLate = calculateLateHours(employeeId, startOfWeek, endOfWeek);
        double totalUndertime = calculateUndertimeHours(employeeId, startOfWeek, endOfWeek);
        double totalOvertime = calculateOvertimeHours(employeeId, startOfWeek, endOfWeek);

        // Format and return the attendance report
        return String.format(
                "==================== Attendance Report ====================\n" +
                "Employee ID   : %s\n" +
                "Employee Name : %s\n" +
                "Period        : %s to %s\n" +
                "Hours Worked  : %.2f\n" +
                "Late          : %.2f\n" +
                "Undertime     : %.2f\n" +
                "Overtime      : %.2f\n" +
                "===========================================================\n",
                employee.getId(),
                employee.getName(),
                startOfWeek,
                endOfWeek,
                totalHoursWorked,
                totalLate,
                totalUndertime,
                totalOvertime
        );
    }

    // Method to generate a team attendance report
    @Override
    public String generateTeamAttendanceReport(String supervisorId, LocalDate date) {
        Employee supervisor = findEmployeeById(supervisorId);
        if (supervisor == null) {
            return "Supervisor not found.";
        }

        List<Employee> teamMembers = findEmployeesBySupervisor(supervisor.getName());
        if (teamMembers.isEmpty()) {
            return "No team members found for the supervisor.";
        }

        // Calculate the start and end dates of the week
        LocalDate startOfWeek = date.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Initialize a StringBuilder to build the report
        StringBuilder report = new StringBuilder();
        report.append(String.format("==================== Team Attendance Report ====================\n"));
        report.append(String.format("Supervisor ID : %s\n", supervisor.getId()));
        report.append(String.format("Supervisor Name: %s\n", supervisor.getName()));
        report.append(String.format("Period        : %s to %s\n\n", startOfWeek, endOfWeek));

        // Loop through team members to generate individual attendance details
        for (Employee member : teamMembers) {
            double totalHoursWorked = calculateTotalHoursWorked(member.getId(), startOfWeek, endOfWeek);
            double totalLate = calculateLateHours(member.getId(), startOfWeek, endOfWeek);
            double totalUndertime = calculateUndertimeHours(member.getId(), startOfWeek, endOfWeek);
            double totalOvertime = calculateOvertimeHours(member.getId(), startOfWeek, endOfWeek);

            // Append individual member's attendance details to the report
            report.append(String.format(
                    "Employee ID   : %s\n" +
                    "Employee Name : %s\n" +
                    "Hours Worked  : %.2f\n" +
                    "Late          : %.2f\n" +
                    "Undertime     : %.2f\n" +
                    "Overtime      : %.2f\n" +
                    "-----------------------------------------------------------\n",
                    member.getId(),
                    member.getName(),
                    totalHoursWorked,
                    totalLate,
                    totalUndertime,
                    totalOvertime
            ));
        }

        // Append the end of the report
        report.append("===========================================================\n");
        return report.toString();
    }
}
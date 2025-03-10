package com.mycompany.payslipmotorphms2.service;

// Import necessary classes and packages
import com.mycompany.payslipmotorphms2.dao.CSVDataLoader;
import com.mycompany.payslipmotorphms2.model.Employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
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

        // Initialize variables to store total hours, late, undertime, and overtime
        double totalHoursWorked = 0.0;
        double totalLate = 0.0;
        double totalUndertime = 0.0;
        double totalOvertime = 0.0;

        // Loop through work hours to calculate total hours, late, undertime, and overtime
        for (String[] record : workHours) {
            if (!record[0].trim().equals(employeeId.trim())) {
                continue;
            }

            try {
                LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                if (logDate.isBefore(startOfWeek) || logDate.isAfter(endOfWeek)) {
                    continue;
                }

                String[] logInParts = record[3].trim().split(":");
                String[] logOutParts = record[4].trim().split(":");
                LocalTime logInTime = LocalTime.of(Integer.parseInt(logInParts[0]), Integer.parseInt(logInParts[1]));
                LocalTime logOutTime = LocalTime.of(Integer.parseInt(logOutParts[0]), Integer.parseInt(logOutParts[1]));

                // Calculate hours worked
                double hoursWorked = (logOutTime.toSecondOfDay() - logInTime.toSecondOfDay()) / 3600.0;
                totalHoursWorked += hoursWorked;

                // Calculate late, undertime, and overtime
                if (logInTime.isAfter(LocalTime.of(9, 0))) {
                    totalLate += (logInTime.toSecondOfDay() - LocalTime.of(9, 0).toSecondOfDay()) / 3600.0;
                }
                if (logOutTime.isBefore(LocalTime.of(18, 0))) {
                    totalUndertime += (LocalTime.of(18, 0).toSecondOfDay() - logOutTime.toSecondOfDay()) / 3600.0;
                }
                if (logInTime.isBefore(LocalTime.of(9, 0))) {
                    totalOvertime += (LocalTime.of(9, 0).toSecondOfDay() - logInTime.toSecondOfDay()) / 3600.0;
                }
                if (logOutTime.isAfter(LocalTime.of(18, 0))) {
                    totalOvertime += (logOutTime.toSecondOfDay() - LocalTime.of(18, 0).toSecondOfDay()) / 3600.0;
                }

            } catch (DateTimeParseException e) {
                // Skip invalid dates
            } catch (Exception e) {
                // Skip invalid time entries
            }
        }

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

        Optional<String> supervisorNameOpt = dataLoader.getSupervisorNameById(supervisorId);
        if (!supervisorNameOpt.isPresent()) {
            return "Supervisor name not found.";
        }

        String supervisorName = supervisorNameOpt.get();
        List<Employee> teamMembers = findEmployeesBySupervisor(supervisorName);
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
        report.append(String.format("Supervisor Name: %s\n", supervisorName));
        report.append(String.format("Period        : %s to %s\n\n", startOfWeek, endOfWeek));

        // Loop through team members to generate individual attendance details
        for (Employee member : teamMembers) {
            double totalHoursWorked = 0.0;
            double totalLate = 0.0;
            double totalUndertime = 0.0;
            double totalOvertime = 0.0;

            for (String[] record : workHours) {
                if (!record[0].trim().equals(member.getId().trim())) {
                    continue;
                }

                try {
                    LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                    if (logDate.isBefore(startOfWeek) || logDate.isAfter(endOfWeek)) {
                        continue;
                    }

                    String[] logInParts = record[3].trim().split(":");
                    String[] logOutParts = record[4].trim().split(":");
                    LocalTime logInTime = LocalTime.of(Integer.parseInt(logInParts[0]), Integer.parseInt(logInParts[1]));
                    LocalTime logOutTime = LocalTime.of(Integer.parseInt(logOutParts[0]), Integer.parseInt(logOutParts[1]));

                    // Calculate hours worked
                    double hoursWorked = (logOutTime.toSecondOfDay() - logInTime.toSecondOfDay()) / 3600.0;
                    totalHoursWorked += hoursWorked;

                    // Calculate late, undertime, and overtime
                    if (logInTime.isAfter(LocalTime.of(9, 0))) {
                        totalLate += (logInTime.toSecondOfDay() - LocalTime.of(9, 0).toSecondOfDay()) / 3600.0;
                    }
                    if (logOutTime.isBefore(LocalTime.of(18, 0))) {
                        totalUndertime += (LocalTime.of(18, 0).toSecondOfDay() - logOutTime.toSecondOfDay()) / 3600.0;
                    }
                    if (logInTime.isBefore(LocalTime.of(9, 0))) {
                        totalOvertime += (LocalTime.of(9, 0).toSecondOfDay() - logInTime.toSecondOfDay()) / 3600.0;
                    }
                    if (logOutTime.isAfter(LocalTime.of(18, 0))) {
                        totalOvertime += (logOutTime.toSecondOfDay() - LocalTime.of(18, 0).toSecondOfDay()) / 3600.0;
                    }

                } catch (DateTimeParseException e) {
                    // Skip invalid dates
                                } catch (Exception e) {
                    // Skip invalid time entries
                }
            }

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

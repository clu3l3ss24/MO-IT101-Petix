package com.mycompany.payslipmotorphms2.service;

// Import necessary classes and packages
import com.mycompany.payslipmotorphms2.model.Employee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

// Implementation of the EmployeeService interface
public class EmployeeServiceImpl implements EmployeeService {
    // Fields to store employee data, work hours, and SSS contributions
    private final List<Employee> employees;
    private final List<String[]> workHours;
    private final List<String[]> sssContributions;

    // Constructor to initialize the fields
    public EmployeeServiceImpl(List<Employee> employees, List<String[]> workHours, List<String[]> sssContributions) {
        this.employees = employees;
        this.workHours = workHours;
        this.sssContributions = sssContributions;
    }

    // Method to find an employee by their ID
    @Override
    public Employee findEmployeeById(String employeeId) {
        return employees.stream()
                .filter(employee -> employee.getId().trim().equalsIgnoreCase(employeeId.trim()))
                .findFirst()
                .orElse(null);
    }

    // Method to format employee information into a readable format
    @Override
    public String formatEmployeeInformation(Employee emp) {
        return String.format(
                "==================== Employee Information ====================\n" +
                "Employee Number   : %s\n" +
                "Employee Name     : %s\n" +
                "Birthday          : %s\n" +
                "Phone Number      : %s\n" +
                "Address           : %s\n" +
                "\n" +
                "Employee Status   : %s\n" +
                "Position          : %s\n" +
                "Employee Supervisor: %s\n" +
                "\n" +
                "SSS Number        : %s\n" +
                "Philhealth Number : %s\n" +
                "TIN Number        : %s\n" +
                "Pag-ibig Number   : %s\n" +
                "===========================================================\n",
                emp.getId(),
                emp.getName(),
                emp.getBirthday(),
                emp.getPhoneNumber(),
                emp.getAddress(),
                emp.getStatus(),
                emp.getPosition(),
                emp.getSupervisor(),
                emp.getSssNumber(),
                emp.getPhilhealthNumber(),
                emp.getTinNumber(),
                emp.getPagIbigNumber()
        );
    }

    // Method to calculate weekly basic salary from monthly salary
    private double calculateWeeklyBasicSalary(double monthlySalary) {
        return monthlySalary / 4.0; // Calculate one week's worth of salary
    }

    // Method to calculate total hours worked by an employee within a week
    private double calculateTotalHoursWorked(String employeeId, LocalDate startOfWeek, LocalDate endOfWeek) {
    return workHours.stream()
            .filter(record -> record[0].trim().equals(employeeId.trim())) // Match Employee ID
            .filter(record -> {
                try {
                    LocalDate logDate = LocalDate.parse(record[2].trim(), DateTimeFormatter.ofPattern("M/d/yyyy"));
                    return !logDate.isBefore(startOfWeek) && !logDate.isAfter(endOfWeek); // Within range
                } catch (DateTimeParseException e) {
                    return false; // Skip invalid dates
                }
            })
            .mapToDouble(record -> {
                try {
                    // Extract login and logout times
                    String logInTime = record[3].trim();
                    String logOutTime = record[4].trim();
                    
                    // Convert to LocalTime
                    LocalTime logIn = LocalTime.parse(logInTime, DateTimeFormatter.ofPattern("h:mm a"));
                    LocalTime logOut = LocalTime.parse(logOutTime, DateTimeFormatter.ofPattern("h:mm a"));

                    // Calculate total worked minutes
                    int totalMinutesWorked = (logOut.getHour() * 60 + logOut.getMinute()) - 
                                             (logIn.getHour() * 60 + logIn.getMinute());

                    // Deduct tardiness
                    int tardinessMinutes = calculateTardiness(logInTime);
                    totalMinutesWorked = Math.max(0, totalMinutesWorked - tardinessMinutes);

                    return totalMinutesWorked / 60.0; // Convert minutes to hours
                } catch (Exception e) {
                    return 0.0; // Skip invalid time entries
                }
            })
            .sum();
}

    // Method to calculate tardiness in minutes
private int calculateTardiness(String logInTime) {
    try {
        // Define shift start time (8:00 AM) and grace period (8:10 AM)
        LocalTime shiftStart = LocalTime.of(8, 0);
        LocalTime gracePeriodEnd = LocalTime.of(8, 10);
        
        // Parse employee login time
        LocalTime logIn = LocalTime.parse(logInTime.trim(), DateTimeFormatter.ofPattern("h:mm a"));
        
        // Check if the employee is late
        if (logIn.isAfter(gracePeriodEnd)) {
            return (int) Duration.between(shiftStart, logIn).toMinutes(); // Total minutes late from 8:00 AM
        }
    } catch (DateTimeParseException e) {
        System.out.println("Invalid log-in time format: " + logInTime);
    }
    return 0; // No tardiness if within grace period or invalid time
}

    
    // Method to calculate SSS contribution based on taxable income
    private double calculateSSSContribution(double totalTaxableIncome) {
        if (totalTaxableIncome < 3250) {
            return 135.0;
        }

        if (totalTaxableIncome > 24750) {
            return 1125.0;
        }

        for (String[] record : sssContributions) {
            try {
                double minRange = Double.parseDouble(record[0].replace(",", "").trim());
                double maxRange = Double.parseDouble(record[2].replace(",", "").trim());
                if (totalTaxableIncome >= minRange && totalTaxableIncome <= maxRange) {
                    return Double.parseDouble(record[3].replace(",", "").trim());
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Skip invalid ranges
            }
        }
        return 0.0;
    }

    // Method to calculate Pag-IBIG contribution based on basic salary
    private double calculatePagibigContribution(double basicSalary) {
        if (basicSalary >= 1000 && basicSalary <= 1500) {
            return basicSalary * 0.01; // 1% of basic salary
        } else if (basicSalary > 1500) {
            return basicSalary * 0.02; // 2% of basic salary
        }
        return 0.0;
    }

    // Method to calculate PhilHealth contribution based on basic salary
    private double calculatePhilhealthContribution(double basicSalary) {
        double premiumRate = 0.03; // 3% of basic salary
        double monthlyPremium = basicSalary * premiumRate;
        return monthlyPremium / 2; // Employee Share (50%)
    }

    // Method to calculate withholding tax based on taxable income
    private double calculateWithholdingTax(double taxableIncome) {
        if (taxableIncome <= 20832) {
            return 0.0;
        } else if (taxableIncome <= 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome <= 66667) {
            return 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome <= 166667) {
            return 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome <= 666667) {
            return 40833.33 + (taxableIncome - 166667) * 0.32;
        } else {
            return 200833.33 + (taxableIncome - 666667) * 0.35;
        }
    }

    // Method to calculate weekly allowance from monthly allowance
    private double calculateAllowance(double monthlyAllowance) {
        return monthlyAllowance / 4.0; // Calculate one week's worth of allowance
    }

    // Method to generate a payslip for an employee
    @Override
    public String generatePayslip(Employee employee, LocalDate date) {
        LocalDate startOfWeek = date.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        double basicSalary = 0.0;
        try {
            basicSalary = calculateWeeklyBasicSalary(Double.parseDouble(employee.getBasicSalary().replace(",", "")));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Basic Salary format for Employee ID: " + employee.getId());
        }

        double hoursWorked = calculateTotalHoursWorked(employee.getId(), startOfWeek, endOfWeek);

        double totalTaxableIncome = 0.0;
        try {
            double hourlyRate = Double.parseDouble(employee.getHourlyRate().replace(",", ""));
            totalTaxableIncome = hoursWorked * hourlyRate;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Hourly Rate format for Employee ID: " + employee.getId());
        }

        double sssContribution = calculateSSSContribution(totalTaxableIncome);
        double pagibigContribution = calculatePagibigContribution(basicSalary);
        double philhealthContribution = calculatePhilhealthContribution(basicSalary);

        double taxableIncomeAfterDeductions = totalTaxableIncome - (sssContribution + pagibigContribution + philhealthContribution);
        double withholdingTax = calculateWithholdingTax(taxableIncomeAfterDeductions);

        double totalDeductions = sssContribution + pagibigContribution + philhealthContribution + withholdingTax;

        // Calculate Non-Taxable Income
        double riceSubsidy = calculateAllowance(Double.parseDouble(employee.getRiceSubsidy().replace(",", "")));
        double phoneAllowance = calculateAllowance(Double.parseDouble(employee.getPhoneAllowance().replace(",", "")));
        double clothingAllowance = calculateAllowance(Double.parseDouble(employee.getClothingAllowance().replace(",", "")));
        double totalNonTaxableIncome = riceSubsidy + phoneAllowance + clothingAllowance;

        // Add tardiness calculation in the payslip generation
int tardinessMinutes = workHours.stream()
    .filter(record -> record[0].trim().equals(employee.getId().trim()))
    .mapToInt(record -> calculateTardiness(record[3].trim()))
    .sum();
        
        // Calculate Net Salary
        double netSalary = (totalTaxableIncome + totalNonTaxableIncome) - totalDeductions;

        // Format and return the payslip
                return String.format(
                "\n==================== MOTORPH | Payslip =====================\n" +
                "Period: %s to %s\n" +
                "------------------------------------------------------------\n" +
                "Employee ID          : %s\n" +
                "Employee Name        : %s\n" +
                "Position             : %s\n" +
                "Status               : %s\n" +
                "Immediate Supervisor : %s\n" +
                "Phone Number         : %s\n" +
                "SSS Number           : %s\n" +
                "Philhealth Number    : %s\n" +
                "TIN Number           : %s\n" +
                "Pag-ibig Number      : %s\n\n" +
                "-------------------- Taxable Earnings ----------------------\n" +
                "Basic Salary         : %.2f\n" +
                "Hours Worked         : %.2f\n" +
                "Total Taxable Income : %.2f\n" +
                "------------------------------------------------------------\n" +
                "---------------------- Deductions --------------------------\n" +
                "SSS                  : %.2f\n" +
                "Pag-ibig             : %.2f\n" +
                "Philhealth           : %.2f\n" +
                "Withholding Tax      : %.2f\n" +
                "Tardiness (minutes)    : %d\n" +
                "Total Deductions     : %.2f\n" +
                "------------------------------------------------------------\n" +
                "-------------------- Non-Taxable Income --------------------\n" +
                "Rice Subsidy         : %.2f\n" +
                "Phone Allowance      : %.2f\n" +
                "Clothing Allowance   : %.2f\n" +
                "Total Non-Taxable Income: %.2f\n" +
                "------------------------------------------------------------\n" +
                "------------------------ Net Salary ------------------------\n" +
                "Total Taxable Income : %.2f\n" +
                "Total Non-Taxable Income: %.2f\n" +
                "Total Deductions     : %.2f\n" +
                "Net Salary           : %.2f\n",
                startOfWeek, endOfWeek,
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getStatus(),
                employee.getSupervisor(),
                employee.getPhoneNumber(),
                employee.getSssNumber(),
                employee.getPhilhealthNumber(),
                employee.getTinNumber(),
                employee.getPagIbigNumber(),
                basicSalary,
                hoursWorked,
                totalTaxableIncome,
                sssContribution,
                pagibigContribution,
                philhealthContribution,
                withholdingTax,
                tardinessMinutes,
                totalDeductions,
                riceSubsidy,
                phoneAllowance,
                clothingAllowance,
                totalNonTaxableIncome,
                totalTaxableIncome,
                totalNonTaxableIncome,
                totalDeductions,
                netSalary
        );
    }
}

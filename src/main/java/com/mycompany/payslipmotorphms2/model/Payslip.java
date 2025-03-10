package com.mycompany.payslipmotorphms2.model;

// Import necessary classes for handling dates
import java.time.LocalDate;

// Class representing a payslip for an employee
public class Payslip {
    // Fields to store payslip details
    private String employeeId;
    private String employeeName;
    private String position;
    private String status;
    private String supervisor;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagibigNumber;
    private LocalDate startOfWeek;
    private LocalDate endOfWeek;
    private double basicSalary;
    private double hoursWorked;
    private double totalTaxableIncome;

    // Constructor to initialize the fields
    public Payslip(String employeeId, String employeeName, String position, String status, String supervisor,
                   String phoneNumber, String sssNumber, String philhealthNumber, String tinNumber,
                   String pagibigNumber, LocalDate startOfWeek, LocalDate endOfWeek, double basicSalary,
                   double hoursWorked, double totalTaxableIncome) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.status = status;
        this.supervisor = supervisor;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
        this.startOfWeek = startOfWeek;
        this.endOfWeek = endOfWeek;
        this.basicSalary = basicSalary;
        this.hoursWorked = hoursWorked;
        this.totalTaxableIncome = totalTaxableIncome;
    }

    // Getter method to retrieve the employee ID
    public String getEmployeeId() {
        return employeeId;
    }

    // Getter method to retrieve the employee name
    public String getEmployeeName() {
        return employeeName;
    }

    // Getter method to retrieve the employee position
    public String getPosition() {
        return position;
    }

    // Getter method to retrieve the employee status
    public String getStatus() {
        return status;
    }

    // Getter method to retrieve the supervisor's name
    public String getSupervisor() {
        return supervisor;
    }

    // Getter method to retrieve the phone number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Getter method to retrieve the SSS number
    public String getSssNumber() {
        return sssNumber;
    }

    // Getter method to retrieve the PhilHealth number
    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    // Getter method to retrieve the TIN number
    public String getTinNumber() {
        return tinNumber;
    }

    // Getter method to retrieve the Pag-IBIG number
    public String getPagibigNumber() {
        return pagibigNumber;
    }

    // Getter method to retrieve the start date of the week
    public LocalDate getStartOfWeek() {
        return startOfWeek;
    }

    // Getter method to retrieve the end date of the week
    public LocalDate getEndOfWeek() {
        return endOfWeek;
    }

    // Getter method to retrieve the basic salary
    public double getBasicSalary() {
        return basicSalary;
    }

    // Getter method to retrieve the hours worked
    public double getHoursWorked() {
        return hoursWorked;
    }

    // Getter method to retrieve the total taxable income
    public double getTotalTaxableIncome() {
        return totalTaxableIncome;
    }

    // Method to generate a formatted payslip string
    public String formatPayslip() {
        return String.format(
                "\n==================== MOTORPH | Payslip ====================\n" +
                "Period: %s to %s\n" +
                "-----------------------------------------------------------\n" +
                "Employee ID          : %s\n" +
                "Employee Name        : %s\n" +
                "Position             : %s\n" +
                "Status               : %s\n" +
                "Immediate Supervisor : %s\n" +
                "Phone Number         : %s\n" +
                "SSS Number           : %s\n" +
                "Philhealth Number    : %s\n" +
                "TIN Number           : %s\n" +
                "Pag-ibig Number      : %s\n" +
                "Basic Salary         : %.2f\n" +
                "Hours Worked         : %.2f\n" +
                "Total Taxable Income : %.2f\n" +
                "-----------------------------------------------------------",
                startOfWeek, endOfWeek,
                employeeId, employeeName, position, status, supervisor,
                phoneNumber, sssNumber, philhealthNumber, tinNumber, pagibigNumber,
                basicSalary, hoursWorked, totalTaxableIncome
        );
    }
}

package com.mycompany.payslipmotorphms2.service;

// Import necessary classes for handling dates
import com.mycompany.payslipmotorphms2.model.Employee;
import java.time.LocalDate;

// Interface for employee services
public interface EmployeeService {
    // Method to find an employee by their ID
    Employee findEmployeeById(String employeeId);

    // Method to generate a payslip for a given employee and date
    String generatePayslip(Employee employee, LocalDate date);

    // Method to format employee information into a readable format
    String formatEmployeeInformation(Employee emp);
}

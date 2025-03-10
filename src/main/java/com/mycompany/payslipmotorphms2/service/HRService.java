package com.mycompany.payslipmotorphms2.service;

// Import the Employee class
import com.mycompany.payslipmotorphms2.model.Employee;

// Interface for HR services
public interface HRService {
    // Method to format employee information along with their allowances
    String formatEmployeeInformationWithAllowances(Employee emp);
}

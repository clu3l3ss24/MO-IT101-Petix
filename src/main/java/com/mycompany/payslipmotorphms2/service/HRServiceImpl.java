package com.mycompany.payslipmotorphms2.service;

// Import the necessary classes
import com.mycompany.payslipmotorphms2.model.Employee;
import com.mycompany.payslipmotorphms2.dao.CSVDataLoader;

import java.util.List;

// Implementation of the HRService interface
public class HRServiceImpl implements HRService {
    // Fields to store employee data and the data loader
    private final List<Employee> employees;
    private final CSVDataLoader dataLoader;

    // Constructor to initialize the fields
    public HRServiceImpl(List<Employee> employees, CSVDataLoader dataLoader) {
        this.employees = employees;
        this.dataLoader = dataLoader;
    }

    // Method to format employee information along with their allowances
    @Override
    public String formatEmployeeInformationWithAllowances(Employee emp) {
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
                "\n" +
                "Basic Salary      : %s\n" +
                "Rice Subsidy      : %s\n" +
                "Phone Allowance   : %s\n" +
                "Clothing Allowance: %s\n" +
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
                emp.getPagIbigNumber(),
                emp.getBasicSalary(),
                emp.getRiceSubsidy(),
                emp.getPhoneAllowance(),
                emp.getClothingAllowance()
        );
    }
}

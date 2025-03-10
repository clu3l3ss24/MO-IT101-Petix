package com.mycompany.payslipmotorphms2.service;

// Import necessary classes for handling employee data and data loading
import com.mycompany.payslipmotorphms2.model.Employee;
import com.mycompany.payslipmotorphms2.dao.CSVDataLoader;

import java.util.List;
import java.util.stream.Collectors;

// Implementation of the TeamService interface
public class TeamServiceImpl implements TeamService {
    // Fields to store employee data and the data loader
    private final List<Employee> employees;
    private final CSVDataLoader dataLoader;

    // Constructor to initialize the fields
    public TeamServiceImpl(List<Employee> employees, CSVDataLoader dataLoader) {
        this.employees = employees;
        this.dataLoader = dataLoader;
    }

    // Method to find team members supervised by a given supervisor
    @Override
    public List<Employee> findTeamMembersBySupervisor(String supervisorId) {
        // Get the supervisor's name using the supervisor ID
        String supervisorName = dataLoader.getSupervisorNameById(supervisorId).orElse("");
        
        // Filter the employees list to find team members supervised by the given supervisor
        return employees.stream()
                .filter(emp -> emp.getSupervisor().equalsIgnoreCase(supervisorName))
                .collect(Collectors.toList());
    }
}

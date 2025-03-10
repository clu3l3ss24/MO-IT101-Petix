package com.mycompany.payslipmotorphms2.dao;

// Import necessary classes and packages
import com.mycompany.payslipmotorphms2.model.Employee;
import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Class to load data from CSV files
public class CSVDataLoader {
    // URLs of the CSV files
    private static final String EMPLOYEE_INFO_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTBLzCIRuzmn7q1IGRwGZPkOwEYxDSc-0hBGVb-L9dRYWzvXALwBPFMYIX5PYM3FqE6bnGs2wXhe8tb/pub?output=csv";
    private static final String HOURS_WORKED_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQIEbVTRKXlXpu66fyzpOuBkHeLjNywGwVwyTD7A16ueojxRZcFzK7rSkBPsoIqeIQJ2zz4su-JeChh/pub?output=csv";
    private static final String SSS_CONTRIBUTIONS_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vRdQ06hunLklriJAtDW1bw-NUlYK_8euUSEyX8SrPrcg_G5A73DNocJ7mKd7rX969tAH1Ou25Xlj8sH/pub?output=csv";

    // List to store employee data
    private List<Employee> employees = new ArrayList<>();
    
    // Map to store supervisor ID to name mapping
    private Map<String, String> supervisorMap = new HashMap<>();

    // Method to load employee data from the CSV file
    public List<Employee> loadEmployees() {
        try {
            // Create a URL object with the EMPLOYEE_INFO_URL
            URL csvUrl = new URL(EMPLOYEE_INFO_URL);
            // Open a stream and read the CSV file
            try (CSVReader reader = new CSVReader(new InputStreamReader(csvUrl.openStream()))) {
                String[] fields;
                reader.readNext(); // Skip header row
                
                // Read each row of the CSV file
                while ((fields = reader.readNext()) != null) {
                    // Trim each field to ensure clean data
                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = fields[i].trim();
                    }
                    
                    // Create an Employee object with the data from the CSV file
                    Employee employee = new Employee(
                            fields[0],  // Employee #
                            fields[1],  // FULL NAME
                            fields[11], // Position
                            fields[12], // Immediate Supervisor
                            fields[5],  // Phone Number
                            fields[6],  // SSS #
                            fields[7],  // Philhealth #
                            fields[8],  // TIN #
                            fields[9],  // Pag-ibig #
                            fields[10], // Status
                            fields[2],  // Birthday
                            fields[3],  // Address
                            fields[13], // Basic Salary (monthly)
                            fields[18], // Hourly Rate
                            fields[16], // Rice Subsidy
                            fields[14], // Phone Allowance
                            fields[15]  // Clothing Allowance
                    );
                    
                    // Add the employee to the list
                    employees.add(employee);
                    
                    // Map the supervisor ID to their name
                    supervisorMap.put(fields[0].trim(), fields[1].trim());
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during file reading
            System.err.println("Error loading employee data: " + e.getMessage());
        }
        
        // Return the list of employees
        return employees;
    }

    // Method to get the supervisor's name by their employee ID
    public Optional<String> getSupervisorNameById(String employeeId) {
        return Optional.ofNullable(supervisorMap.get(employeeId.trim()));
    }

    // Method to check if an employee is a supervisor
    public boolean isSupervisor(String employeeId) {
        return supervisorMap.containsKey(employeeId.trim());
    }

    // Method to load work hours data from the CSV file
    public List<String[]> loadWorkHours() {
        List<String[]> workHours = new ArrayList<>();
        try {
            // Create a URL object with the HOURS_WORKED_URL
            URL csvUrl = new URL(HOURS_WORKED_URL);
            // Open a stream and read the CSV file
            try (CSVReader reader = new CSVReader(new InputStreamReader(csvUrl.openStream()))) {
                String[] fields;
                reader.readNext(); // Skip header row
                
                // Read each row of the CSV file
                while ((fields = reader.readNext()) != null) {
                    // Add the fields to the list of work hours
                    workHours.add(fields);
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during file reading
            System.err.println("Error loading work hours data: " + e.getMessage());
        }
        
        // Return the list of work hours
        return workHours;
    }

    // Method to load SSS contributions data from the CSV file
    public List<String[]> loadSSSContributions() {
        List<String[]> sssContributions = new ArrayList<>();
        try {
            // Create a URL object with the SSS_CONTRIBUTIONS_URL
            URL csvUrl = new URL(SSS_CONTRIBUTIONS_URL);
            // Open a stream and read the CSV file
            try (CSVReader reader = new CSVReader(new InputStreamReader(csvUrl.openStream()))) {
                String[] fields;
                reader.readNext(); // Skip header row
                
                // Read each row of the CSV file
                while ((fields = reader.readNext()) != null) {
                    // Add the fields to the list of SSS contributions
                    sssContributions.add(fields);
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that occur during file reading
            System.err.println("Error loading SSS contributions data: " + e.getMessage());
        }
        
        // Return the list of SSS contributions
        return sssContributions;
    }
}

package com.mycompany.payslipmotorphms2;

// Import necessary classes and packages
import com.mycompany.payslipmotorphms2.service.EmployeeService;
import com.mycompany.payslipmotorphms2.service.EmployeeServiceImpl;
import com.mycompany.payslipmotorphms2.service.AttendanceService;
import com.mycompany.payslipmotorphms2.service.AttendanceServiceImpl;
import com.mycompany.payslipmotorphms2.service.TeamService;
import com.mycompany.payslipmotorphms2.service.TeamServiceImpl;
import com.mycompany.payslipmotorphms2.service.HRService;
import com.mycompany.payslipmotorphms2.service.HRServiceImpl;
import com.mycompany.payslipmotorphms2.dao.CSVDataLoader;
import com.mycompany.payslipmotorphms2.model.Employee;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the CSVDataLoader object to load data from CSV files
        CSVDataLoader dataLoader = new CSVDataLoader();
        
        // Load employee data from the CSV file
        List<Employee> employees = dataLoader.loadEmployees();
        
        // Load work hours data from the CSV file
        List<String[]> workHours = dataLoader.loadWorkHours();
        
        // Load SSS contribution data from the CSV file
        List<String[]> sssContributions = dataLoader.loadSSSContributions();
        
        // Initialize service classes with the loaded data
        EmployeeService employeeService = new EmployeeServiceImpl(employees, workHours, sssContributions);
        AttendanceService attendanceService = new AttendanceServiceImpl(employees, workHours, dataLoader);
        TeamService teamService = new TeamServiceImpl(employees, dataLoader);
        HRService hrService = new HRServiceImpl(employees, dataLoader);

        // Create a Scanner object for capturing user input
        Scanner scanner = new Scanner(System.in);

        // Main loop to display options and process user input
        while (true) {
            // Display the main menu options
            System.out.println("Welcome To MotorPH");
            System.out.println("1. Payslip");
            System.out.println("2. Attendance");
            System.out.println("3. Employee Information");
            System.out.println("4. Exit");
            System.out.print("Please choose an option: ");
            
            // Capture the user's choice
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            // Process the user's choice
            switch (choice) {
                case 1:
                    // Handle payslip generation
                    handlePayslipOption(scanner, employeeService);
                    break;
                case 2:
                    // Handle attendance report generation
                    handleAttendanceOption(scanner, attendanceService, dataLoader);
                    break;
                case 3:
                    // Handle employee information retrieval
                    handleEmployeeInformationOption(scanner, employeeService, teamService, hrService, dataLoader);
                    break;
                case 4:
                    // Exit the application
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
                default:
                    // Handle invalid choice
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to handle payslip generation option
    private static void handlePayslipOption(Scanner scanner, EmployeeService employeeService) {
        // Prompt the user to enter an employee ID
        System.out.print("Enter Employee ID: ");
        String employeeId = scanner.nextLine();
        
        // Prompt the user to enter a date
        System.out.print("Enter Date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        
        // Parse the entered date
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        
        // Find the employee by ID
        Employee employee = employeeService.findEmployeeById(employeeId);
        
        // Check if the employee was found
        if (employee != null) {
            // Generate and display the payslip for the employee
            String payslip = employeeService.generatePayslip(employee, date);
            System.out.println(payslip);
        } else {
            // Display an error message if the employee was not found
            System.out.println("Employee not found.");
        }
    }

    // Method to handle attendance report generation option
    private static void handleAttendanceOption(Scanner scanner, AttendanceService attendanceService, CSVDataLoader dataLoader) {
        // Display the attendance report options
        System.out.println("1. Individual");
        System.out.println("2. Team");
        System.out.print("Please choose an option: ");
        
        // Capture the user's choice
        int attendanceChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Process the user's choice
        if (attendanceChoice == 1) {
            // Handle individual attendance report generation
            System.out.print("Enter Employee ID: ");
            String employeeId = scanner.nextLine();
            
            System.out.print("Enter Date (yyyy-MM-dd): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            
            String attendanceReport = attendanceService.generateIndividualAttendanceReport(employeeId, date);
            System.out.println(attendanceReport);
        } else if (attendanceChoice == 2) {
            // Handle team attendance report generation
            System.out.print("Enter Supervisor ID: ");
            String supervisorId = scanner.nextLine();

            // Check if the entered ID belongs to a supervisor
            if (dataLoader.isSupervisor(supervisorId)) {
                System.out.print("Enter Date (yyyy-MM-dd): ");
                String dateStr = scanner.nextLine();
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                
                String teamAttendanceReport = attendanceService.generateTeamAttendanceReport(supervisorId, date);
                System.out.println(teamAttendanceReport);
            } else {
                // Display an error message if the ID does not belong to a supervisor
                System.out.println("Employee ID is not a supervisor.");
            }
        } else {
            // Handle invalid choice
            System.out.println("Invalid choice. Please try again.");
        }
    }

    // Method to handle employee information retrieval option
    private static void handleEmployeeInformationOption(Scanner scanner, EmployeeService employeeService, TeamService teamService, HRService hrService, CSVDataLoader dataLoader) {
        // Display the employee information options
        System.out.println("1. Individual");
        System.out.println("2. Team");
        System.out.print("Please choose an option: ");
        
        // Capture the user's choice
        int infoChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        // Process the user's choice
        if (infoChoice == 1) {
            // Handle individual employee information retrieval
            System.out.print("Enter Employee ID: ");
            String employeeId = scanner.nextLine();
            
            Employee empInfo = employeeService.findEmployeeById(employeeId);
            if (empInfo != null) {
                // Display the employee information
                System.out.println(employeeService.formatEmployeeInformation(empInfo));
            } else {
                // Display an error message if the employee was not found
                System.out.println("Employee not found.");
            }
        } else if (infoChoice == 2) {
            // Display the team information options
            System.out.println("1. Supervisor");
            System.out.println("2. HR");
            System.out.print("Please choose an option: ");
            
            // Capture the user's choice
            int teamChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            if (teamChoice == 1) {
                // Handle team information retrieval for a supervisor
                System.out.print("Enter Supervisor's Employee ID: ");
                String supervisorId = scanner.nextLine();
                
                // Check if the entered ID belongs to a supervisor
                if (dataLoader.isSupervisor(supervisorId)) {
                    List<Employee> teamMembers = teamService.findTeamMembersBySupervisor(supervisorId);
                    for (Employee teamMember : teamMembers) {
                        // Display the information for each team member
                        System.out.println(employeeService.formatEmployeeInformation(teamMember));
                    }
                } else {
                    // Display an error message if the ID does not belong to a supervisor
                    System.out.println("Employee ID is not a supervisor.");
                }
                        } else if (teamChoice == 2) {
                // Handle team information retrieval for HR
                System.out.print("Enter Employee ID: ");
                String hrId = scanner.nextLine();
                
                Employee hrEmployee = employeeService.findEmployeeById(hrId);
                if (hrEmployee != null && hrEmployee.getPosition().toLowerCase().contains("hr")) {
                    System.out.print("Enter Supervisor's Employee ID: ");
                    String supervisorId = scanner.nextLine();
                    
                    // Check if the entered ID belongs to a supervisor
                    if (dataLoader.isSupervisor(supervisorId)) {
                        List<Employee> teamMembers = teamService.findTeamMembersBySupervisor(supervisorId);
                        for (Employee teamMember : teamMembers) {
                            // Display the information with allowances for each team member
                            System.out.println(hrService.formatEmployeeInformationWithAllowances(teamMember));
                        }
                    } else {
                        // Display an error message if the ID does not belong to a supervisor
                        System.out.println("Employee ID is not a supervisor.");
                    }
                } else {
                    // Display an error message if the ID does not belong to an HR employee
                    System.out.println("Employee ID is not associated with HR.");
                }
            } else {
                // Handle invalid choice for team information
                System.out.println("Invalid choice. Please try again.");
            }
        } else {
            // Handle invalid choice for employee information
            System.out.println("Invalid choice. Please try again.");
        }
    }
}

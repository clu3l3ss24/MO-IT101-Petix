package com.mycompany.payslipmotorphms2.util;

// Import the Scanner class for user input
import java.util.Scanner;

// Class to provide console helper methods
public class ConsoleHelper {
    // Field to store the Scanner object
    private final Scanner scanner;

    // Constructor to initialize the Scanner object
    public ConsoleHelper() {
        this.scanner = new Scanner(System.in);
    }

    // Method to print a custom menu to the console
    public void printCustomMenu() {
        System.out.println("\n===== WELCOME TO MOTOR PH PAYROLL SYSTEM =====");
        System.out.println("1. Payslip");
        System.out.println("2. Attendance");
        System.out.println("3. Employee Information");
        System.out.println("4. Exit");
    }

    // Method to prompt the user for input and return it as a string
    public String promptForInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    // Method to prompt the user for an integer input and return it
    public int promptForInt(String message) {
        System.out.print(message);
        // Loop until a valid integer is entered
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Clear the invalid input
            System.out.print(message);
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover newline
        return choice;
    }
}

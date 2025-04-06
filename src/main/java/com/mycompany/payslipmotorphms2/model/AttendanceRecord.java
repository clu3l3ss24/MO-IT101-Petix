package com.mycompany.payslipmotorphms2.model;

// Import necessary classes for date and time handling
import java.time.LocalDateTime;
import java.time.Duration;

// Class representing an attendance record for an employee
public class AttendanceRecord {
    // Fields to store employee ID, time in, and time out
    private int employeeId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;

    // Constructor to initialize the fields
    public AttendanceRecord(int employeeId, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.employeeId = employeeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    // Getter method to retrieve the employee ID
    public int getEmployeeId() {
        return employeeId;
    }

    // Getter method to retrieve the time in
    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    // Getter method to retrieve the time out
    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    // Method to calculate the total hours worked
    public double getHoursWorked() {
        // Check if both time in and time out are not null
        if (timeIn != null && timeOut != null) {
            // Calculate the duration between time in and time out
            Duration duration = Duration.between(timeIn, timeOut);
            // Return the total hours worked as a decimal
            return duration.toHours() + (duration.toMinutesPart() / 60.0);
        }
        // Return 0.0 if time in or time out is null
        return 0.0;
    }
}
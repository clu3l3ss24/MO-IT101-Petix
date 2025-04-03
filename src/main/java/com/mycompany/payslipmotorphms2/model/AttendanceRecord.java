package com.mycompany.payslipmotorphms2.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class AttendanceRecord {
    private int employeeId;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;

    // Constructor
    public AttendanceRecord(int employeeId, LocalDateTime timeIn, LocalDateTime timeOut) {
        this.employeeId = employeeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    // Getters
    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    // Method to calculate total hours worked with deduction rules
    public double getHoursWorked() {
        if (timeIn != null && timeOut != null) {
            // Define the scheduled login time (8:00 AM) and the allowed grace period (until 8:10 AM)
            LocalDateTime scheduledTimeIn = timeIn.toLocalDate().atTime(8, 0);
            LocalDateTime gracePeriodTime = timeIn.toLocalDate().atTime(8, 10);
            LocalDateTime endOfDay = timeIn.toLocalDate().atTime(17, 0); // 5:00 PM

            // Ensure timeOut is capped at 5:00 PM if after that
            if (timeOut.isAfter(endOfDay)) {
                timeOut = endOfDay;
            }

            // Calculate the duration worked without deduction
            Duration duration = Duration.between(timeIn, timeOut);
            double totalHours = duration.toHours() + (duration.toMinutesPart() / 60.0);

            // If timeIn is after 8:10 AM, apply deduction
            if (timeIn.isAfter(gracePeriodTime)) {
                Duration lateDuration = Duration.between(gracePeriodTime, timeIn);
                double lateMinutes = lateDuration.toMinutes(); // Calculate late minutes
                double deduction = lateMinutes / 60.0; // Convert minutes to hours

                // Subtract the deduction from the total hours worked
                totalHours -= deduction;
            }

            // Round the total hours worked to 2 decimal places
            return Math.round(totalHours * 100.0) / 100.0;
        }
        return 0.0;
    }
}

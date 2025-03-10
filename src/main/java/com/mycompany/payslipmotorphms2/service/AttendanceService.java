package com.mycompany.payslipmotorphms2.service;

// Import necessary classes for handling dates
import java.time.LocalDate;

// Interface for attendance services
public interface AttendanceService {
    // Method to generate an individual attendance report for a given employee ID and date
    String generateIndividualAttendanceReport(String employeeId, LocalDate date);

    // Method to generate a team attendance report for a given supervisor ID and date
    String generateTeamAttendanceReport(String supervisorId, LocalDate date);
}

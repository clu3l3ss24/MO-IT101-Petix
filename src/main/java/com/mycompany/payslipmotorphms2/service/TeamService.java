package com.mycompany.payslipmotorphms2.service;

// Import necessary classes for handling employee data
import com.mycompany.payslipmotorphms2.model.Employee;
import java.util.List;

// Interface for team services
public interface TeamService {
    // Method to find team members supervised by a given supervisor
    List<Employee> findTeamMembersBySupervisor(String supervisorId);
}

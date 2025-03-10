package com.mycompany.payslipmotorphms2.model;

// Class representing an employee
public class Employee {
    // Fields to store employee details
    private String id;
    private String name;
    private String position;
    private String supervisor;
    private String phoneNumber;
    private String sssNumber;
    private String philhealthNumber;
    private String tinNumber;
    private String pagIbigNumber;
    private String status;
    private String birthday;
    private String address;
    private String basicSalary;
    private String hourlyRate;
    private String riceSubsidy;
    private String phoneAllowance;
    private String clothingAllowance;

    // Constructor to initialize the fields
    public Employee(String id, String name, String position, String supervisor, String phoneNumber, 
                    String sssNumber, String philhealthNumber, String tinNumber, String pagIbigNumber,
                    String status, String birthday, String address, String basicSalary, String hourlyRate,
                    String riceSubsidy, String phoneAllowance, String clothingAllowance) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.supervisor = supervisor;
        this.phoneNumber = phoneNumber;
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.status = status;
        this.birthday = birthday;
        this.address = address;
        this.basicSalary = basicSalary;
        this.hourlyRate = hourlyRate;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
    }

    // Getter method to retrieve the employee ID
    public String getId() { return id; }

    // Getter method to retrieve the employee name
    public String getName() { return name; }

    // Getter method to retrieve the employee position
    public String getPosition() { return position; }

    // Getter method to retrieve the employee's supervisor
    public String getSupervisor() { return supervisor; }

    // Getter method to retrieve the employee's phone number
    public String getPhoneNumber() { return phoneNumber; }

    // Getter method to retrieve the employee's SSS number
    public String getSssNumber() { return sssNumber; }

    // Getter method to retrieve the employee's PhilHealth number
    public String getPhilhealthNumber() { return philhealthNumber; }

    // Getter method to retrieve the employee's TIN number
    public String getTinNumber() { return tinNumber; }

    // Getter method to retrieve the employee's Pag-IBIG number
    public String getPagIbigNumber() { return pagIbigNumber; }

    // Getter method to retrieve the employee's status
    public String getStatus() { return status; }

    // Getter method to retrieve the employee's birthday
    public String getBirthday() { return birthday; }

    // Getter method to retrieve the employee's address
    public String getAddress() { return address; }

    // Getter method to retrieve the employee's basic salary
    public String getBasicSalary() { return basicSalary; }

    // Getter method to retrieve the employee's hourly rate
    public String getHourlyRate() { return hourlyRate; }

    // Getter method to retrieve the employee's rice subsidy
    public String getRiceSubsidy() { return riceSubsidy; }

    // Getter method to retrieve the employee's phone allowance
    public String getPhoneAllowance() { return phoneAllowance; }

    // Getter method to retrieve the employee's clothing allowance
    public String getClothingAllowance() { return clothingAllowance; }
}

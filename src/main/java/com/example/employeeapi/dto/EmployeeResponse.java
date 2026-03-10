package com.example.employeeapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employee response")
public class EmployeeResponse {

    @Schema(description = "Unique identifier")
    private Long id;

    @Schema(description = "Employee first name")
    private String firstName;

    @Schema(description = "Employee last name")
    private String lastName;

    @Schema(description = "Employee email address")
    private String email;

    @Schema(description = "Department name")
    private String department;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String firstName, String lastName, String email, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

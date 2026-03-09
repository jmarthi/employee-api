package com.example.employeeapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employee response")
public class EmployeeResponse {

    @Schema(description = "Unique identifier")
    private Long id;

    @Schema(description = "Employee full name")
    private String name;

    @Schema(description = "Employee email address")
    private String email;

    @Schema(description = "Department name")
    private String department;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

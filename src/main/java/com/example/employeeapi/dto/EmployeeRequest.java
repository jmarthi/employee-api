package com.example.employeeapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating or updating an employee")
public class EmployeeRequest {

    @Schema(description = "Employee first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100)
    private String firstName;

    @Schema(description = "Employee last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100)
    private String lastName;

    @Schema(description = "Employee email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 255)
    private String email;

    @Schema(description = "Department name", example = "Engineering")
    @Size(max = 100)
    private String department;

    public EmployeeRequest() {
    }

    public EmployeeRequest(String firstName, String lastName, String email, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
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

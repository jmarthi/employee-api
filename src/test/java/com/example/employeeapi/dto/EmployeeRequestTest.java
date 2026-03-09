package com.example.employeeapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRequestTest {

    @Test
    @DisplayName("default constructor and setters")
    void gettersSetters() {
        EmployeeRequest req = new EmployeeRequest();
        req.setName("John");
        req.setEmail("john@example.com");
        req.setDepartment("IT");
        assertThat(req.getName()).isEqualTo("John");
        assertThat(req.getEmail()).isEqualTo("john@example.com");
        assertThat(req.getDepartment()).isEqualTo("IT");
    }

    @Test
    @DisplayName("all-args constructor")
    void allArgsConstructor() {
        EmployeeRequest req = new EmployeeRequest("Jane", "jane@example.com", "HR");
        assertThat(req.getName()).isEqualTo("Jane");
        assertThat(req.getEmail()).isEqualTo("jane@example.com");
        assertThat(req.getDepartment()).isEqualTo("HR");
    }
}

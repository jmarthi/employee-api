package com.example.employeeapi.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeResponseTest {

    @Test
    @DisplayName("default constructor and setters")
    void gettersSetters() {
        EmployeeResponse res = new EmployeeResponse();
        res.setId(1L);
        res.setFirstName("John");
        res.setLastName("Doe");
        res.setEmail("john@example.com");
        res.setDepartment("IT");
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getFirstName()).isEqualTo("John");
        assertThat(res.getLastName()).isEqualTo("Doe");
        assertThat(res.getEmail()).isEqualTo("john@example.com");
        assertThat(res.getDepartment()).isEqualTo("IT");
    }

    @Test
    @DisplayName("all-args constructor")
    void allArgsConstructor() {
        EmployeeResponse res = new EmployeeResponse(1L, "Jane", "Smith", "jane@example.com", "HR");
        assertThat(res.getId()).isEqualTo(1L);
        assertThat(res.getFirstName()).isEqualTo("Jane");
        assertThat(res.getLastName()).isEqualTo("Smith");
        assertThat(res.getEmail()).isEqualTo("jane@example.com");
        assertThat(res.getDepartment()).isEqualTo("HR");
    }
}

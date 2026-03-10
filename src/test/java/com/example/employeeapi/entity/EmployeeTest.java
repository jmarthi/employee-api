package com.example.employeeapi.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    @DisplayName("default constructor and setters work")
    void defaultConstructorAndSetters() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        employee.setDepartment("IT");

        assertThat(employee.getId()).isEqualTo(1L);
        assertThat(employee.getFirstName()).isEqualTo("John");
        assertThat(employee.getLastName()).isEqualTo("Doe");
        assertThat(employee.getEmail()).isEqualTo("john@example.com");
        assertThat(employee.getDepartment()).isEqualTo("IT");
    }

    @Test
    @DisplayName("all-args constructor sets fields")
    void allArgsConstructor() {
        Employee employee = new Employee("Jane", "Smith", "jane@example.com", "HR");
        assertThat(employee.getFirstName()).isEqualTo("Jane");
        assertThat(employee.getLastName()).isEqualTo("Smith");
        assertThat(employee.getEmail()).isEqualTo("jane@example.com");
        assertThat(employee.getDepartment()).isEqualTo("HR");
    }
}

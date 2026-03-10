package com.example.employeeapi.repository;

import com.example.employeeapi.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("save and findById returns employee")
    void saveAndFindById() {
        Employee employee = new Employee("John", "Doe", "john@example.com", "Engineering");
        Employee saved = employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("findByEmail returns employee when exists")
    void findByEmail_found() {
        Employee employee = new Employee("Jane", "Smith", "jane@example.com", "HR");
        employeeRepository.save(employee);

        Optional<Employee> found = employeeRepository.findByEmail("jane@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Jane");
        assertThat(found.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    @DisplayName("findByEmail returns empty when not exists")
    void findByEmail_notFound() {
        Optional<Employee> found = employeeRepository.findByEmail("nonexistent@example.com");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail returns true when email exists")
    void existsByEmail_true() {
        employeeRepository.save(new Employee("X", "Y", "x@example.com", "IT"));
        assertThat(employeeRepository.existsByEmail("x@example.com")).isTrue();
    }

    @Test
    @DisplayName("existsByEmail returns false when email does not exist")
    void existsByEmail_false() {
        assertThat(employeeRepository.existsByEmail("missing@example.com")).isFalse();
    }

    @Test
    @DisplayName("existsByEmailAndIdNot excludes given id")
    void existsByEmailAndIdNot() {
        Employee e1 = employeeRepository.save(new Employee("A", "One", "same@example.com", "IT"));
        Employee e2 = employeeRepository.save(new Employee("B", "Two", "other@example.com", "HR"));
        assertThat(employeeRepository.existsByEmailAndIdNot("same@example.com", e1.getId())).isFalse();
        assertThat(employeeRepository.existsByEmailAndIdNot("same@example.com", e2.getId())).isTrue();
    }

    @Test
    @DisplayName("findAll returns all employees")
    void findAll() {
        employeeRepository.save(new Employee("A", "One", "a@example.com", "IT"));
        employeeRepository.save(new Employee("B", "Two", "b@example.com", "HR"));
        assertThat(employeeRepository.findAll()).hasSize(2);
    }
}

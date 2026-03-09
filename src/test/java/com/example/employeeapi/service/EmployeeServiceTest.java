package com.example.employeeapi.service;

import com.example.employeeapi.dto.EmployeeRequest;
import com.example.employeeapi.dto.EmployeeResponse;
import com.example.employeeapi.entity.Employee;
import com.example.employeeapi.exception.DuplicateEmailException;
import com.example.employeeapi.exception.EmployeeNotFoundException;
import com.example.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    @DisplayName("createEmployee saves and returns response")
    void createEmployee_success() {
        EmployeeRequest request = new EmployeeRequest("John Doe", "john@example.com", "Engineering");
        Employee saved = new Employee("John Doe", "john@example.com", "Engineering");
        saved.setId(1L);
        when(employeeRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getDepartment()).isEqualTo("Engineering");
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    @DisplayName("createEmployee throws when email exists")
    void createEmployee_duplicateEmail_throws() {
        EmployeeRequest request = new EmployeeRequest("John", "existing@example.com", "IT");
        when(employeeRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("existing@example.com");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateEmployee updates and returns response")
    void updateEmployee_success() {
        Long id = 1L;
        EmployeeRequest request = new EmployeeRequest("Jane Doe", "jane@example.com", "HR");
        Employee existing = new Employee("John", "john@example.com", "IT");
        existing.setId(id);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByEmailAndIdNot("jane@example.com", id)).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeResponse response = employeeService.updateEmployee(id, request);

        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getDepartment()).isEqualTo("HR");
        verify(employeeRepository).save(existing);
    }

    @Test
    @DisplayName("updateEmployee throws when employee not found")
    void updateEmployee_notFound_throws() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.updateEmployee(999L,
                new EmployeeRequest("X", "x@example.com", "Y")))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("999");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateEmployee throws when new email already exists for another")
    void updateEmployee_duplicateEmail_throws() {
        Long id = 1L;
        Employee existing = new Employee("John", "john@example.com", "IT");
        existing.setId(id);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(employeeRepository.existsByEmailAndIdNot("taken@example.com", id)).thenReturn(true);

        assertThatThrownBy(() -> employeeService.updateEmployee(id,
                new EmployeeRequest("John", "taken@example.com", "IT")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("getEmployee returns response when found")
    void getEmployee_success() {
        Employee employee = new Employee("John", "john@example.com", "IT");
        employee.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse response = employeeService.getEmployee(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John");
        verify(employeeRepository).findById(1L);
    }

    @Test
    @DisplayName("getEmployee throws when not found")
    void getEmployee_notFound_throws() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployee(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("getAllEmployees returns list")
    void getAllEmployees_success() {
        Employee e1 = new Employee("John", "john@example.com", "IT");
        e1.setId(1L);
        Employee e2 = new Employee("Jane", "jane@example.com", "HR");
        e2.setId(2L);
        when(employeeRepository.findAll()).thenReturn(List.of(e1, e2));

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John");
        assertThat(result.get(1).getName()).isEqualTo("Jane");
        verify(employeeRepository).findAll();
    }

    @Test
    @DisplayName("getAllEmployees returns empty list")
    void getAllEmployees_empty() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertThat(result).isEmpty();
        verify(employeeRepository).findAll();
    }
}

package com.example.employeeapi.controller;

import com.example.employeeapi.dto.EmployeeRequest;
import com.example.employeeapi.dto.EmployeeResponse;
import com.example.employeeapi.exception.GlobalExceptionHandler;
import com.example.employeeapi.exception.DuplicateEmailException;
import com.example.employeeapi.exception.EmployeeNotFoundException;
import com.example.employeeapi.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("POST /api/employees creates employee and returns 201")
    void createEmployee_returnsCreated() throws Exception {
        EmployeeRequest request = new EmployeeRequest("John Doe", "john@example.com", "Engineering");
        EmployeeResponse response = new EmployeeResponse(1L, "John Doe", "john@example.com", "Engineering");
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.department").value("Engineering"));

        verify(employeeService).createEmployee(any(EmployeeRequest.class));
    }

    @Test
    @DisplayName("POST /api/employees with invalid body returns 400")
    void createEmployee_invalidBody_returnsBadRequest() throws Exception {
        EmployeeRequest request = new EmployeeRequest("", "invalid-email", null);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/employees/{id} updates employee and returns 200")
    void updateEmployee_returnsOk() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Jane Doe", "jane@example.com", "HR");
        EmployeeResponse response = new EmployeeResponse(1L, "Jane Doe", "jane@example.com", "HR");
        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"));

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeRequest.class));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} when not found returns 404")
    void updateEmployee_notFound_returns404() throws Exception {
        EmployeeRequest request = new EmployeeRequest("Jane", "jane@example.com", "HR");
        when(employeeService.updateEmployee(eq(999L), any(EmployeeRequest.class)))
                .thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(put("/api/employees/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/employees/{id} returns employee")
    void getEmployee_returnsOk() throws Exception {
        EmployeeResponse response = new EmployeeResponse(1L, "John", "john@example.com", "IT");
        when(employeeService.getEmployee(1L)).thenReturn(response);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));

        verify(employeeService).getEmployee(1L);
    }

    @Test
    @DisplayName("GET /api/employees/{id} when not found returns 404")
    void getEmployee_notFound_returns404() throws Exception {
        when(employeeService.getEmployee(999L)).thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/employees returns all employees")
    void getAllEmployees_returnsOk() throws Exception {
        List<EmployeeResponse> list = List.of(
                new EmployeeResponse(1L, "John", "john@example.com", "IT"),
                new EmployeeResponse(2L, "Jane", "jane@example.com", "HR")
        );
        when(employeeService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));

        verify(employeeService).getAllEmployees();
    }

    @Test
    @DisplayName("POST with duplicate email returns 400")
    void createEmployee_duplicateEmail_returns400() throws Exception {
        EmployeeRequest request = new EmployeeRequest("John", "existing@example.com", "IT");
        when(employeeService.createEmployee(any(EmployeeRequest.class)))
                .thenThrow(new DuplicateEmailException("existing@example.com"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

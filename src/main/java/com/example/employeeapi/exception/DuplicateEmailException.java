package com.example.employeeapi.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Employee with email already exists: " + email);
    }
}

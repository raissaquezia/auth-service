package com.example.userservice.exceptions;

public class ForbiddenRoleAssignmentException extends RuntimeException {
    public ForbiddenRoleAssignmentException() {
        super("Tentativa de escalonamento de privilégios bloqueada");
    }
}

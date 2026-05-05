package com.example.userservice.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Usuário ou senha inválidos");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
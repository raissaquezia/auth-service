package com.example.userservice.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException() {
        super("Cliente (Sistema) não encontrado");
    }
}

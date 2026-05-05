package com.example.userservice.controllers;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.services.ClientService;
import com.example.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagementController {

    private final UserService userService;
    private final ClientService clientService;

    @PostMapping("/api/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody Dtos.UserRegistrationRequest request) {
        userService.registerUser(request);
    }

    @PostMapping("/api/admin/clients")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Dtos.ClientRegistrationResponse registerClient(@RequestBody Dtos.ClientRegistrationRequest request) {
        return clientService.registerClient(request);
    }
}

package com.example.userservice.controllers;

import com.example.userservice.dtos.Dtos;
import com.example.userservice.entities.User;
import com.example.userservice.services.AuthService;
import com.example.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<Dtos.UserResponse> getMe() {
        User user = authService.getAuthenticatedUser();
        return ResponseEntity.ok(userService.findById(user.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<Dtos.UserResponse> updateMe(@RequestBody Dtos.UserUpdateRequest request) {
        User user = authService.getAuthenticatedUser();
        return ResponseEntity.ok(userService.updateUser(user.getId(), request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        User user = authService.getAuthenticatedUser();
        userService.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }
}

package com.example.userservice.controllers;

import com.example.userservice.entities.roles.Admin;
import com.example.userservice.entities.roles.Customer;
import com.example.userservice.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping
    public Admin save(@RequestBody Admin admin) {
        adminRepository.save(admin);
        return admin;
    }

    @GetMapping
    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Admin findById(@PathVariable UUID id) {
        return adminRepository.findById(id).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable UUID id) {
        adminRepository.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Admin> update(@RequestBody Admin admin, @PathVariable UUID id) {
        return adminRepository.findById(id).map(admin1 -> {
            admin1.setFirstName(admin.getFirstName());
            admin1.setLastName(admin.getLastName());
            admin1.setEmail(admin.getEmail());
            admin1.setPassword(admin.getPassword());

            return adminRepository.save(admin1);
        }).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

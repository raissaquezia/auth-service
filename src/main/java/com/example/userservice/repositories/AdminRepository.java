package com.example.userservice.repositories;

import com.example.userservice.entities.roles.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin,  UUID> {
}

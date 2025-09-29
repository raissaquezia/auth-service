package com.example.userservice.repositories;

import com.example.userservice.entities.roles.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer,  UUID> {}

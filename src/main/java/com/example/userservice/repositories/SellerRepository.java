package com.example.userservice.repositories;

import com.example.userservice.entities.roles.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller,  UUID> {
}

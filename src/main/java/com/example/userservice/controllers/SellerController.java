package com.example.userservice.controllers;

import com.example.userservice.entities.roles.Customer;
import com.example.userservice.entities.roles.Seller;
import com.example.userservice.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers")
public class SellerController {

    @Autowired
    private SellerRepository sellerRepository;

    @PostMapping
    public Seller save(@RequestBody Seller seller) {
        sellerRepository.save(seller);
        return seller;
    }

    @GetMapping
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Seller findById(@PathVariable UUID id) {
        return sellerRepository.findById(id).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable UUID id) {
        sellerRepository.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Seller> update(@RequestBody Seller seller, @PathVariable UUID id) {
        return sellerRepository.findById(id).map(seller1 -> {
            seller1.setFirstName(seller.getFirstName());
            seller1.setLastName(seller.getLastName());
            seller1.setEmail(seller.getEmail());
            seller1.setPassword(seller.getPassword());
            seller1.setAddress(seller.getAddress());
            seller1.setProfilePhoto(seller.getProfilePhoto());
            seller1.setStoreName(seller.getStoreName());

            return sellerRepository.save(seller1);
        }).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

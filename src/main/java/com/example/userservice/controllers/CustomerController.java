package com.example.userservice.controllers;

import com.example.userservice.entities.roles.Customer;
import com.example.userservice.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @GetMapping
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Customer findById(@PathVariable UUID id) {
        return customerRepository.findById(id).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable UUID id) {
        customerRepository.deleteById(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> update(@RequestBody Customer customer, @PathVariable UUID id) {
        return customerRepository.findById(id).map(customer1 -> {
            customer1.setFirstName(customer.getFirstName());
            customer1.setLastName(customer.getLastName());
            customer1.setEmail(customer.getEmail());
            customer1.setPassword(customer.getPassword());
            customer1.setAddress(customer.getAddress());
            customer1.setDocumentId(customer.getDocumentId());
            customer1.setDateOfBirth(customer.getDateOfBirth());

            return customerRepository.save(customer1);
        }).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

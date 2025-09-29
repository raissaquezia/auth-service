package com.example.userservice.entities.roles;

import com.example.userservice.entities.Address;
import com.example.userservice.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer extends User {

    @NotNull
    private String documentId;

    @Embedded
    private Address address;

    private Date dateOfBirth;
}

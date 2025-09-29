package com.example.userservice.entities.roles;

import com.example.userservice.entities.Address;
import com.example.userservice.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sellers")
@Getter
@Setter
public class Seller extends User {

    private String storeName;
    @Embedded
    @NotNull
    private Address address;
    private String profilePhoto;
}

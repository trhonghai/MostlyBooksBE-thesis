package com.myshop.fullstackdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Date dateOfBirth;
    private String sex;
    private boolean enabled;

    @OneToOne
    @JoinColumn(name ="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @OneToMany
    private List<Address> address = new ArrayList<>();


}

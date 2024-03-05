package com.myshop.fullstackdemo.Auth;

import com.myshop.fullstackdemo.model.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String photos;
    private String Phone;
    private Set<Role> roles = new HashSet<>();
}

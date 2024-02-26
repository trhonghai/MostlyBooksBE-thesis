package com.myshop.fullstackdemo.Auth;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}

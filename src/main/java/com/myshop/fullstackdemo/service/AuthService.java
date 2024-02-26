package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.Auth.RegisterRequest;
import com.myshop.fullstackdemo.model.User;

public interface AuthService {
    User createUser(RegisterRequest registerRequest);
}

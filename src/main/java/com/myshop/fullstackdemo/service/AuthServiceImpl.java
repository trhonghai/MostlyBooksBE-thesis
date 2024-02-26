package com.myshop.fullstackdemo.service;


import com.myshop.fullstackdemo.Auth.RegisterRequest;

import com.myshop.fullstackdemo.model.Role;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.RoleRepository;
import com.myshop.fullstackdemo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    private final RoleRepository roleRepository;
    @Override
    public User createUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setLastName(registerRequest.getLastName());
        user.setFirstName(registerRequest.getFirstName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setPhone(registerRequest.getPhone());
        user.setSex(registerRequest.getSex());

        Set<Role> roles = new HashSet<>();
        for ( int roleId:
                registerRequest.getRoleIds() ) {
            roles.add(roleRepository.findById(roleId).orElseThrow());
        }
        user.setRoles(roles);

        user.setRoles(roles);
        user.setEnabled(registerRequest.isEnabled());
        User savedUser = userRepository.save(user);
        return savedUser;

    }


}

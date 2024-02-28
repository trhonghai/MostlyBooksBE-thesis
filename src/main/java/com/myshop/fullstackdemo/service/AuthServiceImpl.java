package com.myshop.fullstackdemo.service;


import com.myshop.fullstackdemo.Auth.RegisterRequest;

import com.myshop.fullstackdemo.model.Cart;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.Role;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.CartRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.RoleRepository;
import com.myshop.fullstackdemo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    @Override
//    public User createUser(RegisterRequest registerRequest) {
//        User user = new User();
//        user.setLastName(registerRequest.getLastName());
//        user.setFirstName(registerRequest.getFirstName());
//        user.setEmail(registerRequest.getEmail());
//        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
//        user.setDateOfBirth(registerRequest.getDateOfBirth());
//        user.setPhone(registerRequest.getPhone());
//        user.setSex(registerRequest.getSex());
//
//        Set<Role> roles = new HashSet<>();
//        for (int roleId : registerRequest.getRoleIds()) {
//            Role role = roleRepository.findById(roleId).orElseThrow();
//            if (!role.getName().equals("Customer")) {
//                roles.add(role);
//            }
//        }
//        user.setRoles(roles);
//        user.setEnabled(registerRequest.isEnabled());
//
//        User savedUser = userRepository.save(user); // Lưu trước đối tượng User
//
//        for (int roleId : registerRequest.getRoleIds()) {
//            Role role = roleRepository.findById(roleId).orElseThrow();
//            if (role.getName().equals("Customer")) {
//                Customer customer = new Customer();
//                Cart cart = new Cart();
//                cartRepository.save(cart);
//                customer.setUser(savedUser); // Sử dụng đối tượng User đã được lưu
//                customer.setCart(cart);
//                customer.setLastName(registerRequest.getLastName());
//                customer.setFirstName(registerRequest.getFirstName());
//                customer.setEmail(registerRequest.getEmail());
//                customer.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
//                customer.setDateOfBirth(registerRequest.getDateOfBirth());
//                customer.setPhoneNumber(registerRequest.getPhone());
//                customer.setSex(registerRequest.getSex());
//                customer.set
//                customerRepository.save(customer);
//            }
//        }
//        return savedUser;
//    }
    public User createUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setLastName(registerRequest.getLastName());
        user.setFirstName(registerRequest.getFirstName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setPhone(registerRequest.getPhone());
        user.setSex(registerRequest.getSex());

        Role defaultRole = roleRepository.findById(5).get();

        Set<Role> roles = new HashSet<>();
        List<Integer> roleIds = registerRequest.getRoleIds();
        if (roleIds != null) {
            for (int roleId : roleIds) {
                roles.add(roleRepository.findById(roleId).orElseThrow());
            }
        }
        if (roles.isEmpty()) {
            roles.add(defaultRole);
        }
        user.setRoles(roles);

        user.setRoles(roles);
        user.setEnabled(registerRequest.isEnabled() == false ? registerRequest.isEnabled() : true);
        User savedUser = userRepository.save(user);


        if (roles.contains(defaultRole)) {
            Customer customer = new Customer();
            Cart cart = new Cart();
            cartRepository.save(cart);
            customer.setUser(savedUser);
            customer.setCart(cart);
            customer.setLastName(registerRequest.getLastName());
            customer.setFirstName(registerRequest.getFirstName());
            customer.setEmail(registerRequest.getEmail());
            customer.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
            customer.setDateOfBirth(registerRequest.getDateOfBirth());
            customer.setPhone(registerRequest.getPhone());
            customer.setSex(registerRequest.getSex());
            customer.setEnabled(true);
            customerRepository.save(customer);
        }

        return savedUser;
    }




}

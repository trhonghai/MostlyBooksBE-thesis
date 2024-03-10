package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.exception.NotFoundException;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-infor")
public class CustomerController {
    private final CustomerRepository customerRepository;

    @GetMapping("/{id}")
    Customer getCustomer(@PathVariable Long id){
        return customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer " +
                "not found, " +
                "id="+id));
    }

    @PutMapping("/update-customer/{id}")
    Customer updateCustomer(@RequestBody Customer newUser, @PathVariable Long id){
        System.out.println("newUser" + newUser);
        Customer customer = customerRepository.findById(id).get();
        customer.setFirstName(newUser.getFirstName());
        customer.setLastName(newUser.getLastName());
        customer.setDateOfBirth(newUser.getDateOfBirth());
        customer.setPhone(newUser.getPhone());
        customer.setSex(newUser.getSex());
        customer.setEmail(newUser.getEmail());
        return customerRepository.save(customer);
    }



}

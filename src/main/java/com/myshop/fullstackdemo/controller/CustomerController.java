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
        return customerRepository.findById(id).map(customer ->{
            customer.setFirstName(newUser.getFirstName());
            customer.setLastName(newUser.getLastName());
            customer.setPhone(newUser.getPhone());
            customer.setSex(newUser.getSex());
            customer.setDateOfBirth(newUser.getDateOfBirth());
            return customerRepository.save(customer);
        }).orElseThrow(()->new NotFoundException("Customer " +
                "not found, " +
                "id="+id));
    }



}

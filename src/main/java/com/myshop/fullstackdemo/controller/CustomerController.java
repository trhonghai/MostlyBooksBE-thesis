package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.exception.NotFoundException;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-infor")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    @GetMapping("/{id}")
    Customer getCustomer(@PathVariable Long id){
        return customerRepository.findById(id).orElseThrow(()->new NotFoundException("Customer " +
                "not found, " +
                "id="+id));
    }

    @PutMapping("/update-customer/{id}")
    Customer updateCustomer(@RequestBody Map<String , String> newUser, @PathVariable Long id) throws ParseException {
        System.out.println("newUser" + newUser);
        String email = newUser.get("email");
        String firstName = newUser.get("firstName");
        String lastName = newUser.get("lastName");
        String phone = newUser.get("phone");
        String dateOfBirthString = newUser.get("dateOfBirth");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = dateFormat.parse(dateOfBirthString);

        String sex = newUser.get("sex");

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found, id=" + id));
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setDateOfBirth(dateOfBirth);
        customer.setPhone(phone);
        customer.setSex(sex);


        Long userId = customer.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found, id=" + userId));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setDateOfBirth(dateOfBirth);
        user.setSex(sex);


        return customerRepository.save(customer);
    }



}

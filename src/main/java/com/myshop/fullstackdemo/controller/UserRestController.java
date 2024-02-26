package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@Param("email") String email) {
        boolean isUnique = userService.isEmailUnique(email);
//        System.out.println();
        return isUnique ? "OK" : "Duplicated";
    }

}

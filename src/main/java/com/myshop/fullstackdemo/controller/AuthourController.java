package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Authour;
import com.myshop.fullstackdemo.repository.AuthourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authour")
public class AuthourController {
    private final AuthourRepository authourRepository;
    @GetMapping
    List<Authour> getAllAuthour() {
        return authourRepository.findAll();
    }
}

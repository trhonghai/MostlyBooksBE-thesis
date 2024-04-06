package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Authour;
import com.myshop.fullstackdemo.repository.AuthourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    Authour getAuthour(@PathVariable Integer id) {
        return authourRepository.findById(id).orElseThrow();
    }

    @PostMapping("/new")
    Authour newAuthour(@RequestBody Authour authour) {
        return authourRepository.save(authour);
    }

    @PutMapping("/update/{id}")
    Authour updateAuthour(@RequestBody Authour authour, @PathVariable Integer id) {
        Authour authourToUpdate = authourRepository.findById(id).orElseThrow();
        authourToUpdate.setName(authour.getName());
        return authourRepository.save(authourToUpdate);
    }

    @DeleteMapping("/delete/{id}")
    void deleteAuthour(@PathVariable Integer id) {
        authourRepository.deleteById(id);
    }

}

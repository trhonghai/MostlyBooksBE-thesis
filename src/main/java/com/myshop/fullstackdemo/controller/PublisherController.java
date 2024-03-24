package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Publisher;
import com.myshop.fullstackdemo.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publisher")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherRepository publisherRepository;
    @GetMapping
    List<Publisher> getAllPublisher(){
        return  publisherRepository.findAll();
    }
}

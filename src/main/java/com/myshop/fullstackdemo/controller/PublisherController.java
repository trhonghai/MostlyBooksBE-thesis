package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Publisher;
import com.myshop.fullstackdemo.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    Publisher addPublisher(@RequestBody Publisher publisher){
        publisher.setName(publisher.getName());
        publisher.setEmail(publisher.getEmail());
        publisher.setPhone(publisher.getPhone());
        publisher.setPhotos(publisher.getPhotos());
        publisher.setAddress(publisher.getAddress());
        return publisherRepository.save(publisher);
    }
}

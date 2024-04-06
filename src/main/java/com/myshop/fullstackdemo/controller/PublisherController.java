package com.myshop.fullstackdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.fullstackdemo.model.Publisher;
import com.myshop.fullstackdemo.repository.PublisherRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.File;
import com.stripe.model.FileLink;
import com.stripe.param.FileCreateParams;
import com.stripe.param.FileLinkCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publisher")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherRepository publisherRepository;

    @GetMapping
    List<Publisher> getAllPublisher() {
        return publisherRepository.findAll();
    }

    @PostMapping("/add")
    Publisher addPublisher(@RequestParam("data") String publisher,@RequestPart("image") MultipartFile multipartFile) throws IOException, StripeException {

        ObjectMapper objectMapper = new ObjectMapper();
        Publisher newPublisher = objectMapper.readValue(publisher, Publisher.class);
        newPublisher.setName(newPublisher.getName());
        newPublisher.setEmail(newPublisher.getEmail());
        newPublisher.setPhone(newPublisher.getPhone());
        newPublisher.setAddress(newPublisher.getAddress());
        if (multipartFile != null) {
            Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";
            byte[] img_data;
            try {
                img_data = multipartFile.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String currentWorkingDirectory = System.getProperty("user.dir");
            System.out.println("name=" + multipartFile.getOriginalFilename());
            String filePath = currentWorkingDirectory + "/" + multipartFile.getOriginalFilename();
            java.io.File fileNew = new java.io.File(filePath);
            java.io.FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new java.io.FileOutputStream(filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                fileOutputStream.write(img_data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            com.stripe.param.FileCreateParams fileCreateParams = com.stripe.param.FileCreateParams.builder()
                    .setFile(fileNew)
                    .setPurpose(com.stripe.param.FileCreateParams.Purpose.DISPUTE_EVIDENCE)
                    .build();
            File file = null;
            try {
                file = File.create(fileCreateParams);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }

            FileLinkCreateParams params =
                    FileLinkCreateParams.builder()
                            .setFile(file.getId())
                            .build();
            FileLink fileLink;
            try {
                fileLink = FileLink.create(params);
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }

            fileNew.delete();
            newPublisher.setPhotos(fileLink.getUrl());
        }
        return publisherRepository.save(newPublisher);
    }

    @GetMapping("/{id}")
    Publisher getPublisher(@PathVariable Integer id) {
        return publisherRepository.findById(id).orElseThrow();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Publisher> updatePublisher(@RequestParam("data") String publisherData,
                                                     @PathVariable Integer id,
                                                     @RequestPart(value = "image", required = false) MultipartFile multipartFile)
            throws IOException, StripeException {

        ObjectMapper objectMapper = new ObjectMapper();
        Publisher updatedPublisher = objectMapper.readValue(publisherData, Publisher.class);
       return publisherRepository.findById(id)
                .map(publisher -> {
                    publisher.setName(updatedPublisher.getName());
                    publisher.setEmail(updatedPublisher.getEmail());
                    publisher.setPhone(updatedPublisher.getPhone());
                    publisher.setAddress(updatedPublisher.getAddress());
                    publisher.setPhotos(updatedPublisher.getPhotos());
                    if (multipartFile != null) {
                        Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";
                        byte[] img_data;
                        try {
                            img_data = multipartFile.getBytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String currentWorkingDirectory = System.getProperty("user.dir");
                        System.out.println("name=" + multipartFile.getOriginalFilename());
                        String filePath = currentWorkingDirectory + "/" + multipartFile.getOriginalFilename();
                        java.io.File fileNew = new java.io.File(filePath);
                        java.io.FileOutputStream fileOutputStream;
                        try {
                            fileOutputStream = new java.io.FileOutputStream(filePath);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            fileOutputStream.write(img_data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        com.stripe.param.FileCreateParams fileCreateParams = com.stripe.param.FileCreateParams.builder()
                                .setFile(fileNew)
                                .setPurpose(com.stripe.param.FileCreateParams.Purpose.DISPUTE_EVIDENCE)
                                .build();
                        File file = null;
                        try {
                            file = File.create(fileCreateParams);
                        } catch (StripeException e) {
                            throw new RuntimeException(e);
                        }

                        FileLinkCreateParams params =
                                FileLinkCreateParams.builder()
                                        .setFile(file.getId())
                                        .build();
                        FileLink fileLink;
                        try {
                            fileLink = FileLink.create(params);
                        } catch (StripeException e) {
                            throw new RuntimeException(e);
                        }

                        fileNew.delete();
                        publisher.setPhotos(fileLink.getUrl());
                    }
                    return ResponseEntity.ok(publisherRepository.save(publisher));
                })
                .orElseGet(() -> {
                    return ResponseEntity.ok(publisherRepository.save(updatedPublisher));
                });


    }

    @DeleteMapping("/delete/{id}")
    void deletePublisher(@PathVariable Integer id) {
        publisherRepository.deleteById(id);
    }
}



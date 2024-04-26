package com.myshop.fullstackdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.fullstackdemo.exception.UserNotFoundException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.File;
import com.stripe.model.FileLink;
import com.stripe.param.FileCreateParams;
import com.stripe.param.FileLinkCreateParams;
import org.springframework.http.ResponseEntity;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.RoleRepository;
import com.myshop.fullstackdemo.repository.UserRepository;
import com.myshop.fullstackdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;



@PostMapping(value = "/users")
    User newUser(@RequestParam String newUser,
                              @RequestPart("image") MultipartFile multipartFile  ) throws IOException, StripeException {
    Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";

        byte[] img_data;

    img_data = multipartFile.getBytes();
    String currentWorkingDirectory = System.getProperty("user.dir");
    System.out.println("name="+multipartFile.getOriginalFilename());
    String filePath = currentWorkingDirectory + "/"+multipartFile.getOriginalFilename();
    java.io.File fileNew = new java.io.File(filePath);

    FileOutputStream fileOutputStream;
    fileOutputStream = new FileOutputStream(filePath);
    fileOutputStream.write(img_data);
    fileOutputStream.close();

        FileCreateParams fileCreateParams = FileCreateParams.builder()
            .setFile(fileNew)
            .setPurpose(FileCreateParams.Purpose.DISPUTE_EVIDENCE)
            .build();
         File file = File.create(fileCreateParams);

         FileLinkCreateParams params =
            FileLinkCreateParams.builder()
                    .setFile(file.getId())
                    .build();
         FileLink fileLink = FileLink.create(params);

         fileNew.delete();

//         System.out.println(fileLink.getUrl()); saved Database

         ObjectMapper objectMapper = new ObjectMapper();
        User addPublisherDto = objectMapper.readValue(newUser, User.class);
        System.out.println(addPublisherDto);
        System.out.println(multipartFile.getOriginalFilename());
        addPublisherDto.setPhotos(fileLink.getUrl());
        return service.save(addPublisherDto);

    }

    @GetMapping("/users")
    ResponseEntity<List<User>> all(){
        return ResponseEntity.ok(userRepository.findAll());
    }



    @GetMapping("/users/{id}")
    User getUser(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestParam String newUser,
                                           @PathVariable Long id,
                                           @RequestPart(value = "image", required = false)
                                               MultipartFile multipartFile) throws IOException ,StripeException {
        System.out.println("newUser = " + newUser);
    ObjectMapper objectMapper = new ObjectMapper();
        User newUserEntity = objectMapper.readValue(newUser, User.class);
    return userRepository.findById(id)
                .map(user -> {
                    user.setLastName(newUserEntity.getLastName());
                    user.setFirstName(newUserEntity.getFirstName());
                    user.setEmail(newUserEntity.getEmail());
                    user.setRoles(newUserEntity.getRoles());
                    user.setEnabled(newUserEntity.isEnabled());
                    if(multipartFile!=null){
                        Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";
                        byte[] img_data;
                        try {
                            img_data = multipartFile.getBytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String currentWorkingDirectory = System.getProperty("user.dir");
                        System.out.println("name="+multipartFile.getOriginalFilename());
                        String filePath = currentWorkingDirectory + "/"+multipartFile.getOriginalFilename();
                        java.io.File fileNew = new java.io.File(filePath);

                        FileOutputStream fileOutputStream;
                        try {
                            fileOutputStream = new FileOutputStream(filePath);
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

                        FileCreateParams fileCreateParams = FileCreateParams.builder()
                                .setFile(fileNew)
                                .setPurpose(FileCreateParams.Purpose.DISPUTE_EVIDENCE)
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
                        FileLink fileLink = null;
                        try {
                            fileLink = FileLink.create(params);
                        } catch (StripeException e) {
                            throw new RuntimeException(e);
                        }

                        fileNew.delete();
                        user.setPhotos(fileLink.getUrl());

                    }
                    return ResponseEntity.ok(userRepository.save(user));
                }).orElseThrow(()->new UserNotFoundException(id));
    }
    @DeleteMapping("/users/{id}")
    String deleteUser(@PathVariable Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        return "User with id "+id+" has been deleted success.";
    }




}

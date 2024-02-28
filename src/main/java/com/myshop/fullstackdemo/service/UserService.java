package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.exception.UserNotFoundException;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class
UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User save(User user){
        encoderPassword(user);
        return userRepo.save(user);
    }
    private void encoderPassword(User user){
        System.out.println(user.getPassword());
        String encoderPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encoderPassword);
    }

    public boolean isEmailUnique(String email){
        User existingUser = userRepo.findByEmail(email).orElseThrow();

        // Kiểm tra xem có user nào có email giống nhau không
        return existingUser == null;
    }

    public User get(Long id) throws UserNotFoundException{
        try {
            return userRepo.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new UserNotFoundException(id);
        }
    }
}

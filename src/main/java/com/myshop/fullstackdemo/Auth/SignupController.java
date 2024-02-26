package com.myshop.fullstackdemo.Auth;

import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:3000")
public class SignupController {
   @Autowired
    private AuthService authService;

   @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        User createdUser = authService.createUser(registerRequest);
        System.out.println(registerRequest);
        if(createdUser ==  null){
            return new ResponseEntity<>("User is not created, try again later", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
//    public ResponseEntity<AuthResponse> signup(@RequestBody AuthRequest authRequest){
//        return ResponseEntity.ok(authService.signup(authRequest));
//    }
}

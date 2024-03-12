package com.myshop.fullstackdemo.Auth;


import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.Role;
import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.UserRepository;
import com.myshop.fullstackdemo.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired CustomerRepository customerRepository;

    @PostMapping("/authentication")
    public AuthenticationResponse createAuthenticationToken(


            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Email or Password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not created. Register User first.");
            return null;
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwtToken = jwtService.generateToken(userDetails.getUsername());
        final String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());
        final Optional<Customer> customer = customerRepository.findCustomerByEmail(authenticationRequest.getEmail());
        final Optional<User> user = userRepository.findByEmail(authenticationRequest.getEmail());

        if (customer.isPresent()) {
            return new CusAuthenticateResponse(jwtToken, refreshToken, customer.get().getCart().getId(), customer.get().getId());
        } else if (user.isPresent()){
            List<String> roles = user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList());
            return new AuthenticationResponse(jwtToken, refreshToken, roles, user.get().getId());
        }
        {
            return new AuthenticationResponse();
        }

    }




}

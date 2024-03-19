package com.myshop.fullstackdemo.security;


import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers("/categories/**").permitAll()
                                .requestMatchers("/roles").permitAll()
                                .requestMatchers("/books/**").permitAll()
                                .requestMatchers("/books/new").hasRole("Admin")
//                                .requestMatchers("/books/detailImages/**").permitAll()
                                .requestMatchers("/api/users/authentication").permitAll()
                                .requestMatchers("/user-cart/**").permitAll()
                                .requestMatchers("/user-infor/**").permitAll()
                                .requestMatchers("/provinces/**").permitAll()
                                .requestMatchers("/payment/create").permitAll()
                                .requestMatchers("/api/checkout/webhook").permitAll()
                                .requestMatchers("/customer/order/**").permitAll()
                                .requestMatchers("/api/paypal/**").permitAll()
                                .requestMatchers("/api/payment/**").permitAll()
                                .anyRequest().authenticated()
                )
//                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer
                                .logoutUrl("/api/users/authentication/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()));

        return http.build();
    }
}




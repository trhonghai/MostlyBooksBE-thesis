package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    User findFirstByEmail(String email);


}

package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}

package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

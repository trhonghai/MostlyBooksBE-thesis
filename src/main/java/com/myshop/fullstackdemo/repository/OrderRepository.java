package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomerId(long customerId);
    Order findByOrderCode(String orderCode);
}

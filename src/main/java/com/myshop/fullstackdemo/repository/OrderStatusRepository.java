package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long>{
}

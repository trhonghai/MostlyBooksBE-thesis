package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}

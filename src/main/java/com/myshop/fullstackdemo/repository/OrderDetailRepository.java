package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailByOrderId (Long orderId);
}

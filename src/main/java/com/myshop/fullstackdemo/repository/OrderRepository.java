package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.model.OrderStatus;
import com.myshop.fullstackdemo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomerId(long customerId);

    Order findByOrderCode(String orderCode);

    List<Order> findByOrderStatus_Status(Status status);
}

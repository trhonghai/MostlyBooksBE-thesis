package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.model.OrderStatus;
import com.myshop.fullstackdemo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomerId(long customerId);

    Order findByCaptureId(String captureCode);

    Order findByOrderCode(String orderCode);

    List<Order> findByOrderStatus_Status(Status status);

    List<Order> findByOrderStatusStatusAndOrderDateBefore(Status status, LocalDateTime localDateTime);

}

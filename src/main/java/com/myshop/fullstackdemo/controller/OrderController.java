package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.model.OrderDetail;
import com.myshop.fullstackdemo.model.OrderStatus;
import com.myshop.fullstackdemo.model.Status;
import com.myshop.fullstackdemo.repository.OrderDetailRepository;
import com.myshop.fullstackdemo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/order")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Order>> getOrderBycustomerId(@PathVariable long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetailById(@PathVariable long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping("/filterStatus")
    public List<Order> getOrdersByStatus(@RequestParam("status") Status status) {
        System.out.println("status: " + status);
        return orderRepository.findByOrderStatus_Status(status);
    }

    @PutMapping("/cancelled/{orderId}")
    public ResponseEntity<Order> cancelledOrder(@PathVariable long orderId ) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.get().getOrderStatus().setStatus(Status.CANCELLED);
        return new ResponseEntity<>(orderRepository.save(order.get()), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable long orderId) {
        orderRepository.deleteById(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.controller.PaymentRequest;
import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Order createOrder(PaymentRequest paymentRequest) {

        if(paymentRequest.getOrderDetailsId() == null || paymentRequest.getOrderDetailsId().isEmpty()){
            throw new RuntimeException("Order details are required");
        }

        if(paymentRequest.getCustomerId() == 0){
            throw new RuntimeException("Customer id is required");
        }

        Order order =new   Order();


        OrderStatus orderStatus = OrderStatus.builder().status(Status.PENDING).orders(new ArrayList<>()).build();
        orderStatusRepository.save(orderStatus);

        Payment payment = Payment.builder()
                .date(new Date())
                .paymentMethod("paypal")
                .build();
        paymentRepository.save(payment);

        Customer customer = customerRepository.findById(paymentRequest.getCustomerId()).orElse(null);
        orderRepository.save(order);
        Address address = customer.getAddress().stream().filter(Address::isDefaultForShopping).findFirst().get();


        List<OrderDetail> orderDetailsList = new ArrayList<>();
        for (Long orderDetailId : paymentRequest.getOrderDetailsId()) {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new IllegalArgumentException("Order detail not found with id: " + orderDetailId));
            orderDetail.setOrder(order);
            orderDetailsList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        double Amount = orderDetailsList.stream().mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity()).sum();

        order.setAmount(Amount);
        order.setOrderDate(new Date());
        order.setCustomer(customer);
        order.setShipping(10000);
        order.setOrderStatus(orderStatus);
        order.setAddress(address);
        order.setPayment(payment);
        order.setOrderDetails(orderDetailsList);
        orderRepository.save(order);

        return order;
    }
}

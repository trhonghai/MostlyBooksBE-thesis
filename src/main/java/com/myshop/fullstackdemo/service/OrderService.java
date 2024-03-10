package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.controller.PaymentRequest;
import com.myshop.fullstackdemo.model.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    Order CreateOrder(PaymentRequest paymentRequest);
}

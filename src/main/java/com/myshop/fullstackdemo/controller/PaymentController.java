package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
     private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;
    @PostMapping("/cash-on-delivery")
    public Order cashOnDelivery(@RequestBody PaymentRequest paymentRequest){
        Order order = new Order();
        OrderStatus orderStatus = OrderStatus.builder().status(Status.PENDING).orders(new ArrayList<>()).build();
        orderStatusRepository.save(orderStatus);

        Payment payment = Payment.builder()
                .date(new Date())
                .paymentMethod("cash-on-delivery")
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

//        order.setOrderCode(orderId);
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

    @PutMapping("/delivery/{orderId}")
    public Order deliveryOrder(@PathVariable long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        order.getOrderStatus().setStatus(Status.SHIPPED);
        return orderRepository.save(order);
    }

    @PutMapping("/delivered/{orderId}")
    public Order deliveredOrder(@PathVariable long orderId){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null){
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        order.getOrderStatus().setStatus(Status.DELIVERED);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Book book = orderDetail.getBook();
            Long quantitySold = orderDetail.getQuantity();

            // Cập nhật số lượng đã bán của sản phẩm
            if (book.getSold() != null) {
                // Cập nhật số lượng đã bán của sản phẩm
                book.setSold(book.getSold() + quantitySold);
            } else {
                // Nếu trường sold là null, cập nhật số lượng đã bán bằng số lượng mới
                book.setSold(quantitySold);
            }

            // Lưu thay đổi vào cơ sở dữ liệu
            bookRepository.save(book);
        }


        return orderRepository.save(order);
    }

    @PutMapping("/capture-cash-on-delivery/{id}")
    public Order captureCashOnDelivery(@PathVariable long id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null){
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        order.getOrderStatus().setStatus(Status.CAPTURED);
        return orderRepository.save(order);
    }

    @PutMapping("/cancel-cash-on-delivery/{id}")
    public Order cancelCashOnDelivery(@PathVariable long id){
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null){
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        order.getOrderStatus().setStatus(Status.CANCELLED);
        return orderRepository.save(order);
    }



}

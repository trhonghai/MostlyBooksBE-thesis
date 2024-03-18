package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.controller.PaymentRequest;
import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Order createOrder(PaymentRequest paymentRequest, String orderId) {

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
        order.setOrderCode(orderId);
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

    public Order captureOrder(String orderCode, String captureId){
        Order order = orderRepository.findByOrderCode(orderCode);
        order.getOrderStatus().setStatus(Status.CAPTURED);
        order.setCaptureId(captureId);
        orderRepository.save(order);
        return order;
    }

    public Order refundOrder (String captureId){
        Order order = orderRepository.findByCaptureId(captureId);
        order.getOrderStatus().setStatus(Status.REFUNDED);
        orderRepository.save(order);
        return order;
    }

    public Order cancelOrder (String orderCode){
        Order order = orderRepository.findByOrderCode(orderCode);
        order.getOrderStatus().setStatus(Status.CANCELLED);
        orderRepository.save(order);
        return order;
    }



    // Lập lịch cho việc xóa đơn hàng hàng ngày vào lúc 00:00
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteCancelledOrders() {
        // Lấy ngày hôm qua
        LocalDate yesterday = LocalDate.now().minusDays(2);

        // Lấy tất cả các đơn hàng có trạng thái "Cancelled" và được tạo trước ngày hôm qua
        List<Order> cancelledOrders = orderRepository.findByOrderStatusStatusAndOrderDateBefore(Status.CANCELLED, yesterday.atStartOfDay());

        // Xóa các đơn hàng
        for (Order order : cancelledOrders) {
            orderRepository.delete(order);
        }

        System.out.println("Cancelled orders older than 1 day have been deleted.");
    }

}

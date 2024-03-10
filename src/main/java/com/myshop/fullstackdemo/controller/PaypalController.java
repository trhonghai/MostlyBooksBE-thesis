package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.config.paypal.PaypalService;
import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.service.OrderServiceImpl;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {
    private final PaypalService paypalService;
    private final OrderServiceImpl orderService;

    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {


            Order order = orderService.createOrder(paymentRequest);
            String cancelUrl = "http://localhost:3000";
            String successUrl = "http://localhost:3000";
            Payment payment = paypalService.createPayment(paymentRequest.getAmount(), paymentRequest.getCurrency(),
                    "paypal", "sale", paymentRequest.getDescription(), cancelUrl, successUrl);
            String approvalUrl = null;
            for (Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    approvalUrl = link.getHref();
                    break;
                }
            }
            if (approvalUrl != null) {
                // Nếu approval_url được tìm thấy, chuyển hướng người dùng đến đó
                PaymentResponse paymentInfo = new PaymentResponse(payment.getId(), payment.getPayer(), approvalUrl);
                return ResponseEntity.ok().body(paymentInfo);
            } else {
                // Nếu không tìm thấy approval_url, trả về lỗi
                return ResponseEntity.badRequest().body("Approval URL not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Payment creation failed.");
        }
    }


//    @GetMapping("/payment/success")
//    public ResponseEntity<String> paymentSuccess(@RequestParam("paymentId") String paymentId) {
//        try {
//            // Xử lý thanh toán thành công với paymentIds
//            String eventData = "Payment with ID " + paymentId + " is successful.";
//            // Trả về sự kiện thành công và dữ liệu sự kiện
//            return ResponseEntity.status(HttpStatus.OK).body(eventData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Trả về lỗi nếu có lỗi xảy ra
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment success.");
//        }
//    }

    @PostMapping("api/checkout/webhook")
    public ResponseEntity<String> hanldePaypalWebhook(@RequestBody String webhookData) {
        log.info("Webhook received: " + webhookData);
        return ResponseEntity.ok().body("Webhook received");
    }

}

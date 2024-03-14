package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.config.paypal.PaypalService;
import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.service.OrderServiceImpl;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/paypal")
public class PaypalController {
    private final String  BASE = "https://api-m.sandbox.paypal.com";
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final OrderServiceImpl orderService;

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }


    public String generateAccessToken(){
        String auth = this.getAuth("AWMswA26MJWfe7tNcTBz5ZzW9LOx4ifuDzrydpAcjeN2ERERLzZdNC5wLru3OQ7eT9Zp1XgkyeVFDLiT",
                "ECI19LsLe2m49_p__pdR0otyENgJUnhXRfgEoZ-IBtGyzE66BDAdHPH1VNqIrlaZWM2JqY11baOM1Lkz");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE +"/v1/oauth2/token",
                request,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.log(Level.INFO, "GET TOKEN: SUCCESSFUL!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.log(Level.SEVERE, "GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }

    }

    @PostMapping("/orders/{orderId}/capture")
    public Object capturePayment(@PathVariable("orderId") String orderId){
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/payments/captures/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            return  response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }

    }

    @PostMapping("/orders/create")
    public Object createOrder (@RequestBody PaymentRequest paymentRequest){
        System.out.println("paymentRequest" + paymentRequest);
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        Order order = orderService.createOrder(paymentRequest);

        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("intent", "CAPTURE");
        requestBody.put("purchase_units", new JSONArray()
                .put(new JSONObject()
                        .put("amount", new JSONObject()
                                .put("currency_code", paymentRequest.getCurrency())
                                .put("value", paymentRequest.getAmount())
                        )
                )
        );

        HttpEntity<?> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            return response.getBody(); // Trả về nội dung của phản hồi từ PayPal
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }




//    @PostMapping("/payment/create")
//    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest paymentRequest) {
//        try {
//            Order order = orderService.createOrder(paymentRequest);
//            String cancelUrl = "http://localhost:3000";
//            String successUrl = "http://localhost:3000";
//            Payment payment = paypalService.createPayment(paymentRequest.getAmount(), paymentRequest.getCurrency(),
//                    "paypal", "sale", paymentRequest.getDescription(), cancelUrl, successUrl);
//            String approvalUrl = null;
//            for (Links link : payment.getLinks()) {
//                if ("approval_url".equals(link.getRel())) {
//                    approvalUrl = link.getHref();
//                    break;
//                }
//            }
//            if (approvalUrl != null) {
//                // Nếu approval_url được tìm thấy, chuyển hướng người dùng đến đó
//                PaymentResponse paymentInfo = new PaymentResponse(payment.getId(), payment.getPayer(), approvalUrl);
//                return ResponseEntity.ok().body(paymentInfo);
//            } else {
//                // Nếu không tìm thấy approval_url, trả về lỗi
//                return ResponseEntity.badRequest().body("Approval URL not found.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Payment creation failed.");
//        }
//    }






}

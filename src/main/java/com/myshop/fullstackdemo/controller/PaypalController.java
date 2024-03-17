package com.myshop.fullstackdemo.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.fullstackdemo.config.paypal.PaypalService;
import com.myshop.fullstackdemo.model.Order;
import com.myshop.fullstackdemo.service.OrderServiceImpl;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
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
    private String getCaptureIdFromResponse(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.convertValue(response, Map.class);

        List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) responseMap.get("purchase_units");
        Map<String, Object> firstPurchaseUnit = purchaseUnits.get(0);
        Map<String, Object> payments = (Map<String, Object>) firstPurchaseUnit.get("payments");
        List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
        Map<String, Object> firstCapture = captures.get(0);
        return (String) firstCapture.get("id");
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
                BASE + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            String captureId = getCaptureIdFromResponse(response.getBody());
            System.out.println("captureId" + captureId);
            Order order = orderService.captureOrder(orderId, captureId);
            return  response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }

    }

    @PostMapping("/orders/create")
    public Object createOrder (@RequestBody PaymentRequest paymentRequest) throws IOException {
        System.out.println("paymentRequest" + paymentRequest);
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

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

            System.out.println("response" + response.getBody().toString());
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString( response.getBody());
            System.out.println("jsonString" + jsonString);

            String orderId = new JSONObject(jsonString).getString("id");
            System.out.println("orderId" + orderId);

//

            Order order = orderService.createOrder(paymentRequest, orderId);
            return response.getBody(); // Trả về nội dung của phản hồi từ PayPal
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }


    @PostMapping("/orders/{captureId}/refund")
    public Object refundPayment(@PathVariable("captureId") String captureId){
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/payments/captures/" + captureId + "/refund",
                HttpMethod.POST,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "REFUND CREATED");
            Order order = orderService.refundOrder(captureId);
            return  response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING REFUND");
            return "Unavailable to get CREATE A REFUND, STATUS CODE " + response.getStatusCode();
        }

    }


    @DeleteMapping("/orders/{orderId}/cancelled")
    public Object cancelOrder(@PathVariable("orderId") String orderCode){
        String token = this.generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v1/checkout/orders/" + orderCode,
                HttpMethod.DELETE,
                entity,
                Object.class
        );
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            LOGGER.log(Level.INFO, "ORDER CANCELLED");
            Order order = orderService.cancelOrder(orderCode);
            return  response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CANCELLING ORDER");
            return "Unavailable to get CANCEL AN ORDER, STATUS CODE " + response.getStatusCode();
        }

    }







}

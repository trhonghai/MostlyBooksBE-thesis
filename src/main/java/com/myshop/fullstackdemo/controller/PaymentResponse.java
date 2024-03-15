package com.myshop.fullstackdemo.controller;

import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String payment;
    private Payer payer;
    private String approvalUrl;
}

package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.config.paypal.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaypalController {
    private final PaypalService paypalService;

    @PostMapping("/payment/create")
    public RedirectView createPayment(){
        try {
            String cancelUrl = "http://localhost:8080/payment/cancel";
            String successUrl = "http://localhost:8080/payment/success";
            Payment payment = paypalService.createPayment( 10.0, "USD", "sale", "Payment description", cancelUrl, successUrl);
         for(Links links: payment.getLinks()){
             if(links.equals("approval_url")){
                 return new RedirectView(links.getHref());
             }
         }
        } catch (Exception e) {
            e.printStackTrace();
            return new RedirectView("/payment/cancel");
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("payerID") String payerId){
       try {
           Payment payment = paypalService.executePayment(paymentId, payerId);
           if(payment.getState().equals("approved")){
               return "payment success";
           }
         } catch (Exception e) {
              e.printStackTrace();
              return "payment failed";

       }
        return "payment success";
    }

}

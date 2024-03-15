package com.myshop.fullstackdemo.config.paypal;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        com.paypal.api.payments.Amount amount = new com.paypal.api.payments.Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

        com.paypal.api.payments.Transaction transaction = new com.paypal.api.payments.Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setIntent(intent);
        payment.setPayer(new com.paypal.api.payments.Payer());
        payment.getPayer().setPaymentMethod(method);
        payment.setTransactions(java.util.List.of(transaction));
        payment.setRedirectUrls(new com.paypal.api.payments.RedirectUrls());
        payment.getRedirectUrls().setCancelUrl(cancelUrl);
        payment.getRedirectUrls().setReturnUrl(successUrl);
//        payment.getPayer().setPayerInfo(new PayerInfo());
        try {
            return payment.create(apiContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public Payment executePayment(String paymentId, String payerId) {
            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            try {
                return payment.execute(apiContext, paymentExecution);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

}

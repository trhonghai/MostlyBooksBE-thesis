package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
}

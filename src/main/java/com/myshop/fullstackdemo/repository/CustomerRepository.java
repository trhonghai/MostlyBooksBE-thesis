package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}

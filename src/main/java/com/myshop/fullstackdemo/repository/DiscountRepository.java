package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Discount;
import com.myshop.fullstackdemo.model.DiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long>{


}

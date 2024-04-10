package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.DiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Long> {
   DiscountDetail findDiscountDetailByBookId(Long id);

    DiscountDetail findDiscountDetailByDiscountId(Long id);
}

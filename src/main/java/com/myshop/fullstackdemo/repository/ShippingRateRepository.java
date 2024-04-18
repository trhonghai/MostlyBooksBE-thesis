package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.ShippingRate;
import com.myshop.fullstackdemo.model.provinces.Province;
import com.paypal.api.payments.ShippingCost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Long> {
    ShippingRate findByProvince(Province province);
}

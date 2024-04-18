package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.ShippingRate;
import com.myshop.fullstackdemo.model.provinces.Province;
import com.myshop.fullstackdemo.repository.ProvinceRepository;
import com.myshop.fullstackdemo.repository.ShippingRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingRateService {
    private final ShippingRateRepository shippingRateRepository;
    private final ProvinceRepository provinceRepository;


//    public ShippingRate addShippingCost(ShippingRate shippingRate) {
//
//        Province province = provinceRepository.findByCode(shippingRate.getProvince().getCode());
//        ShippingRate shippingCost = new ShippingRate();
//        shippingCost.setPrice(shippingRate.getPrice());
//        shippingCost.setDay(shippingRate.getDay());
//
//        shippingRate.setProvince(province);
//        return shippingRateRepository.save(shippingRate);
//    }




}

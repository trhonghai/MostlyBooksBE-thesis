package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.dto.ShippingRequest;
import com.myshop.fullstackdemo.model.ShippingRate;
import com.myshop.fullstackdemo.model.provinces.Province;
import com.myshop.fullstackdemo.repository.ProvinceRepository;
import com.myshop.fullstackdemo.repository.ShippingRateRepository;
import com.myshop.fullstackdemo.service.ShippingRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping-rate")
@RequiredArgsConstructor
public class ShippingRateController {
    private final ProvinceRepository provinceRepository;
    private final ShippingRateRepository shippingRateRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addShippingRate(@RequestBody ShippingRequest shippingRequest) {

        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setPrice(shippingRequest.getPrice());
        shippingRate.setDay(shippingRequest.getDay());
        Province province = provinceRepository.findByCode(shippingRequest.getProvinceCode());
        System.out.println("Province: " + province);
        shippingRate.setProvince(province);
        shippingRateRepository.save(shippingRate);
        return ResponseEntity.ok("Shipping rate added successfully");


    }

    @GetMapping("/get-all")
    public ResponseEntity<Iterable<ShippingRate>> getAllShippingRates() {
        return ResponseEntity.ok(shippingRateRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ShippingRate> getShippingRate(@PathVariable Long id) {
        return ResponseEntity.ok(shippingRateRepository.findById(id).get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShippingRate(@PathVariable Long id) {
        shippingRateRepository.deleteById(id);
        return ResponseEntity.ok("Shipping rate deleted successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateShippingRate(@PathVariable Long id, @RequestBody ShippingRequest shippingRequest) {
        ShippingRate shippingRate = shippingRateRepository.findById(id).get();
        shippingRate.setPrice(shippingRequest.getPrice());
        shippingRate.setDay(shippingRequest.getDay());
        Province province = provinceRepository.findByCode(shippingRequest.getProvinceCode());
        shippingRate.setProvince(province);
        shippingRateRepository.save(shippingRate);
        return ResponseEntity.ok("Shipping rate updated successfully");
    }
    @GetMapping("/get")
    public ResponseEntity<ShippingRate> getShippingRate(@RequestParam String provinceName) {
        Province province = provinceRepository.findByName(provinceName);
        if (province == null) {
            return ResponseEntity.notFound().build();
        }
        ShippingRate shippingRate = shippingRateRepository.findByProvince(province);
        return ResponseEntity.ok(shippingRate);
    }


}

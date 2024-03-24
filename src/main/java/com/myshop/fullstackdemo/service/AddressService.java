package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Address;
import com.myshop.fullstackdemo.model.provinces.Province;
import com.myshop.fullstackdemo.repository.AddressRepository;
import com.myshop.fullstackdemo.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public void setDefaultForShopping(Long addressId, Long customerId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Address not found"));
        ;

        // Tìm địa chỉ mặc định hiện tại của khách hàng
        List<Address> defaultAddress = addressRepository.findAllAddressByCustomerId(customerId);
        for (Address a : defaultAddress) {
            if (a.getId() != addressId) {
                a.setDefaultForShopping(false);
                addressRepository.save(a);
            }
            address.setDefaultForShopping(true);
            addressRepository.save(address);
        }
    }
}

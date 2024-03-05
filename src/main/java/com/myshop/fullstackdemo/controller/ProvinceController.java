package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.exception.NotFoundException;
import com.myshop.fullstackdemo.model.Address;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    @GetMapping
    public List<Map<String, Object>> getAll(){
        return provinceRepository.findAll().stream()
                .map(province -> {
                    Map<String, Object> provinceData = new HashMap<>();
                    provinceData.put("code", province.getCode());
                    provinceData.put("name", province.getName());
                    return provinceData;
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/deleteAddress/{addressId}")
    public ResponseEntity<?> deleteAddressShipping(@PathVariable("addressId") Long addressId){
        Address address = addressRepository.findById(addressId).
                orElseThrow(()->new NotFoundException("Address not found, id="+addressId));
        addressRepository.delete(address);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/districts")
    public List<Map<String, Object>> findDistrictByProvinceName(@RequestParam("provinceCode") String pcode){
        return districtRepository.findAllByProvinceCode(pcode).stream().map(district -> {
            Map<String, Object> districtData = new HashMap<>();
                    districtData.put("code", district.getCode());
                    districtData.put("name", district.getName());
            return districtData;
        }).collect(Collectors.toList());
    }
    @GetMapping("/wards")
        public List<Map<String, Object>> findWardsByDistrictCode(@RequestParam("districtCode")String dcode){
        return wardRepository.findAllByDistrictCode(dcode).stream().map(ward -> {
            Map<String, Object> wardData = new HashMap<>();
            wardData.put("code", ward.getCode());
            wardData.put("name", ward.getName());
            return wardData;
        }).collect(Collectors.toList());
    }

    @GetMapping("/address/{customerId}")
    public List<Address> findAddressByCustomerId(@PathVariable("customerId") Long customerId){
        return addressRepository.findAllAddressByCustomerId(customerId);
    }
    @PostMapping("/newAddress/{customerId}")
    public ResponseEntity<Address> createAddressShipping(@PathVariable("customerId") Long customerId,
                                                         @RequestBody Address address){
        Customer customer = customerRepository.findById(customerId).orElseThrow(()->new NotFoundException("Customer " +
                "not found, " +
                "id="+customerId));
        Address newAddress = new Address();
        newAddress.setCity(address.getCity());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setWard(address.getWard());
        newAddress.setCustomer(customer);
        newAddress.setPhoneNumber(address.getPhoneNumber());
        newAddress.setLastName(address.getLastName());
        newAddress.setFirstName(address.getFirstName());
        newAddress.setDefaultForShopping(address.isDefaultForShopping());
        newAddress.setAddress(address.getAddress());
        addressRepository.save(newAddress);
        return ResponseEntity.ok(newAddress);
    }

    @PutMapping("/updateAddress/{addressId}")
    public ResponseEntity<Address> updateAddressShipping(@PathVariable("addressId") Long addressId,
                                                         @RequestBody Address address){
        Address newAddress = addressRepository.findById(addressId).orElseThrow(()->new NotFoundException("Address " +
                "not found, " +
                "id="+addressId));
        newAddress.setCity(address.getCity());
        newAddress.setDistrict(address.getDistrict());
        newAddress.setWard(address.getWard());
        newAddress.setPhoneNumber(address.getPhoneNumber());
        newAddress.setLastName(address.getLastName());
        newAddress.setFirstName(address.getFirstName());
        newAddress.setDefaultForShopping(address.isDefaultForShopping());
        newAddress.setAddress(address.getAddress());
        addressRepository.save(newAddress);
        return ResponseEntity.ok(newAddress);
    }

}

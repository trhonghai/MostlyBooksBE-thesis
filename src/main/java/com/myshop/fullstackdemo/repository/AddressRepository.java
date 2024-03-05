package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllAddressByCustomerId(Long customerId);
}

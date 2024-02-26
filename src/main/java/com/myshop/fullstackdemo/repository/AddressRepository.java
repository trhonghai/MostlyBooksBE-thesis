package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}

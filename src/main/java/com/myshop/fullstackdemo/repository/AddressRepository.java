package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllAddressByCustomerId(Long customerId);

//   @Query("SELECT a FROM Address a WHERE a.defaultForShopping = true")
//    Address findDefaultAddress();
}

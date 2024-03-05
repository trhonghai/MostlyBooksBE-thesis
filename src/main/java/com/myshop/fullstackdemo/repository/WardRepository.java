package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.provinces.District;
import com.myshop.fullstackdemo.model.provinces.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward ,Long> {
    List<Ward> findAllByDistrictCode(String districtCode);

}

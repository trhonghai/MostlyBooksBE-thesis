package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAllByProvinceCode(String provinceCode);
}

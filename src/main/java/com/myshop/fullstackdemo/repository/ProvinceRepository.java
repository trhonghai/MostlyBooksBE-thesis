package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.provinces.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
}

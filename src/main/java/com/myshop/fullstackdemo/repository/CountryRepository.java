package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    public List<Country> findAllByOrderByNameAsc();
}



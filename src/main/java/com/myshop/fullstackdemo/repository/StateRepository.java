package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Country;
import com.myshop.fullstackdemo.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {
    public List<State> findByCountryOrderByName(Country country);
}

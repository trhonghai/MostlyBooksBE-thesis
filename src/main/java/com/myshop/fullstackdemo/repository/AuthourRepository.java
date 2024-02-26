package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Authour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthourRepository extends JpaRepository<Authour, Integer> {
}

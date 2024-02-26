package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
}

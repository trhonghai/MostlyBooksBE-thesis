package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findByBookId(Long bookId);
}

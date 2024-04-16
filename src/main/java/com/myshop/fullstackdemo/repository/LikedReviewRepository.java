package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.LikedReview;
import com.myshop.fullstackdemo.model.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedReviewRepository extends JpaRepository<LikedReview, Long>{
    LikedReview findByReviewId( Long reviewId);

    boolean existsByReviewIdAndCustomerId(Long reviewId, Long customerId);

    LikedReview findByReviewIdAndCustomerId(Long reviewId, Long customerId);
}

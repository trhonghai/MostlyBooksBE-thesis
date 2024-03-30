package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
import com.stripe.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private  ReviewsRepository reviewsRepository;

    public void createReview(Reviews review) {
        reviewsRepository.save(review);
    }

    public double calculateRating(Long bookId) {
        List<Reviews> reviews = reviewsRepository.findByBookId(bookId);
        if (reviews.isEmpty()) {
            return 0;
        }
        double totalRating = 0;
        for (Reviews review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();
    }

    public Integer totalReviewCount (Long bookId) {
        List<Reviews> reviews = reviewsRepository.findByBookId(bookId);
        return reviews.size();
    }
}

package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.LikedReview;
import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.LikedReviewRepository;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikedReviewService {
    private final ReviewsRepository reviewsRepository;
    private final CustomerRepository customerRepository;
    private final LikedReviewRepository likedReviewRepository;

    public void likeReview(Long reviewId, Long customerId) {
        Reviews review = reviewsRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        LikedReview likedReview = new LikedReview();
        likedReview.setReview(review);
        likedReview.setCustomer(customer);

        likedReviewRepository.save(likedReview);

        // Increase like count in the review entity
        review.setLiked(review.getLiked() + 1);
        reviewsRepository.save(review);
    }

    public void unlikeReview(Long likedReviewId) {
        LikedReview likedReview = likedReviewRepository.findByReviewId(likedReviewId);

        // Decrease like count in the review entity
        Reviews review = likedReview.getReview();
        review.setLiked(review.getLiked() - 1);
        reviewsRepository.save(review);

        likedReviewRepository.delete(likedReview);
    }

    public boolean isReviewLikedByCustomer(Long reviewId, Long customerId) {
        return likedReviewRepository.existsByReviewIdAndCustomerId(reviewId, customerId);
    }
}

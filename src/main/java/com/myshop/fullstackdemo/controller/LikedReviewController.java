package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.dto.LikedReviewRequest;
import com.myshop.fullstackdemo.model.LikedReview;
import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.LikedReviewRepository;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
import com.myshop.fullstackdemo.service.LikedReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/liked-reviews")
@RequiredArgsConstructor
public class LikedReviewController {

    private final LikedReviewService likedReviewService;
    private final ReviewsRepository reviewsRepository;
    private final LikedReviewRepository likedReviewRepository;

    @PostMapping
    public ResponseEntity<?> likeReview(@RequestBody LikedReviewRequest request) {
        likedReviewService.likeReview(request.getReviewId(), request.getCustomerId());
        return ResponseEntity.ok("Review liked");
    }
    @DeleteMapping("/{likedReviewId}")
    public ResponseEntity<?> unlikeReview(@PathVariable Long likedReviewId) {
        likedReviewService.unlikeReview(likedReviewId);
        return ResponseEntity.ok("Review unliked");
    }

    @GetMapping("/{reviewId}/customer/{customerId}")
    public ResponseEntity<Boolean> checkIfReviewLikedByCustomer(@PathVariable Long reviewId, @PathVariable Long customerId) {
        Reviews review = reviewsRepository.findById(reviewId).orElse(null);
        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LikedReview likedReview = likedReviewRepository.findByReviewIdAndCustomerId(reviewId, customerId);
        boolean isLiked = likedReview != null;
        return ResponseEntity.ok(isLiked);
    }
}

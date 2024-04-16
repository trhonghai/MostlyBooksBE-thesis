package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.dto.LikedReviewRequest;
import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
import com.myshop.fullstackdemo.service.LikedReviewService;
import com.myshop.fullstackdemo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    private final ReviewsRepository reviewsRepository;
    private final BookRepository bookRepository;
    private final LikedReviewService likedReviewService;
    @PostMapping("/create/{bookId}")
    public ResponseEntity<?> createReview(@RequestBody Reviews review, @PathVariable Long bookId
    ) {

      Book book = bookRepository.findById(bookId).get();

        review.setBook(book);
        reviewService.createReview(review);

        int totalReviewCount = reviewService.totalReviewCount(bookId);
        book.setReviewCount(totalReviewCount + 1);

        // Cập nhật lại rating cho sách
        double newRating = reviewService.calculateRating(bookId);
        System.out.println("newRating = " + newRating);

        // Sử dụng DecimalFormat để làm tròn số với một số lượng cụ thể của chữ số sau dấu thập phân
        DecimalFormat df = new DecimalFormat("#.#");
        df.setMaximumFractionDigits(1);
        double roundedRating = Double.parseDouble(df.format(newRating));

        book.setRating((float) roundedRating);
        bookRepository.save(book);

        return ResponseEntity.ok("Review created and book rating updated");
    }



    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        return ResponseEntity.ok(reviewsRepository.findAll());
    }
    @GetMapping("/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable Long reviewId) {
        Optional<Reviews> review = reviewsRepository.findById(reviewId);
        if (review.isPresent()) {
            return ResponseEntity.ok(review.get());
        }
        return ResponseEntity.ok("Review not found");
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewsRepository.deleteById(reviewId);
        return ResponseEntity.ok("Review deleted");
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> getReviewsByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewsRepository.findByBookId(bookId));
    }

//    @PutMapping("/{reviewId}")
//    public ResponseEntity<?> updateLiked (@PathVariable Long reviewId,@RequestParam("method") String method) {
//        Optional<Reviews> review = reviewsRepository.findById(reviewId);
//        if (review.isPresent()) {
//            Reviews review1 = review.get();
//            if (method.equals("like")) {
//                review1.setLiked(review1.getLiked() + 1);
//            } else if (method.equals("removeLike")) {
//                review1.setLiked(review1.getLiked() - 1);
//            }
//            reviewsRepository.save(review1);
//            return ResponseEntity.ok("Liked updated");
//        }
//        return ResponseEntity.ok("Review not found");
//    }



//    @PostMapping
//    public ResponseEntity<?> likeReview(@RequestBody LikedReviewRequest request) {
//        likedReviewService.likeReview(request.getReviewId(), request.getCustomerId());
//        return ResponseEntity.ok("Review liked");
//    }
//    @DeleteMapping("/{likedReviewId}")
//    public ResponseEntity<?> unlikeReview(@PathVariable Long likedReviewId) {
//        likedReviewService.unlikeReview(likedReviewId);
//        return ResponseEntity.ok("Review unliked");
//    }



}

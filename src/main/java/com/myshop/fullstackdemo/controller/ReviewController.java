package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
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



}

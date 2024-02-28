package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.Reviews;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.ReviewsRepository;
import com.stripe.model.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ReviewsRepositoryTests {

    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired private CustomerRepository customerRepository;


    @Test
    public void testCreateReviews(){
        long bookId = 1;
        Book book = bookRepository.findById(bookId).get();
        long customerId = 5;
        Customer customer = customerRepository.findById(customerId).get();

        Reviews review = new Reviews();
        review.setComment("Perfect for my needs. Loving it!");
        review.setRating(5);
        review.setDate(new Date());
        review.setBook(book);
        review.setCustomer(customer);

        Reviews savedReview = reviewsRepository.save(review);
        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);



    }
}

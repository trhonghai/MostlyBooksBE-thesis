package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.DiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "(:minPrice IS NULL OR b.price BETWEEN :minPrice AND :maxPrice) " +
            "AND (:categoryName IS NULL OR b.category.name = :categoryName) " +
            "AND (:publisherName IS NULL OR b.publisher.name = :publisherName) " +
            "AND (:coverType IS NULL OR b.cover = :coverType OR b.cover IS NULL OR b.cover = '')")
    List<Book> findBooksByFilters(Float minPrice, Float maxPrice, String categoryName, String publisherName, String coverType);


    @Query("SELECT p FROM Book p WHERE " +
                  "p.name LIKE CONCAT('%',:query, '%')" +
                  "Or p.description LIKE CONCAT('%', :query, '%')")
    List<Book> searchProducts(String query);


}

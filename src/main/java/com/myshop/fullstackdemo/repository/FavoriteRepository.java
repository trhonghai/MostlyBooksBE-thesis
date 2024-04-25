package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByCustomerId(Long customerId);

    boolean existsByCustomerIdAndBookId(Long customerId, Long bookId);

    Optional<Favorite> findByCustomerIdAndBookId(Long customerId, Long bookId);
}

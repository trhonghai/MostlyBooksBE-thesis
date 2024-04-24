package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByCustomerId(Long customerId);
}

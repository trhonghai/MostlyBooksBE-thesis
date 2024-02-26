package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
}

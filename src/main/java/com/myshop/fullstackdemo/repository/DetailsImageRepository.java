package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.DetailsImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsImageRepository extends JpaRepository<DetailsImage,Long> {
}

package com.myshop.fullstackdemo.repository;

import com.myshop.fullstackdemo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}

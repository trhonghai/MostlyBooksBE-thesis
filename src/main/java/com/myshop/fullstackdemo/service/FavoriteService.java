package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.model.Favorite;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import com.myshop.fullstackdemo.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final FavoriteRepository    favoriteRepository;
    public void addFavorite (Customer customer, Book book) {
        Favorite favorite = Favorite.builder()
                .customer(customer)
                .book(book)
                .build();
        favoriteRepository.save(favorite);

    }

    public List<Favorite> getFavorites(Long customerId) {
        return favoriteRepository.findAllByCustomerId(customerId);
    }
}

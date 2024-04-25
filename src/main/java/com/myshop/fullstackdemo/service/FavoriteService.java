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
import java.util.Optional;

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

    public boolean isFavorite(Long customerId, Long bookId) {
        // Kiểm tra xem có một bản ghi trong favoriteRepository có khách hàng và sách tương ứng không
        return favoriteRepository.existsByCustomerIdAndBookId(customerId, bookId);
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    public Long getFavoriteId(Long customerId, Long bookId) {
        Optional<Favorite> favorite = favoriteRepository.findByCustomerIdAndBookId(customerId, bookId);
        return favorite.map(Favorite::getId).orElse(null);
    }
}

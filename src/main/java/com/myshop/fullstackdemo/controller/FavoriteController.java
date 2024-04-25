package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Favorite;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final BookRepository bookRepository;
    @PostMapping("/new")
    public ResponseEntity<String> addFavorite(@RequestBody Favorite request) {
        Book book = bookRepository.findById(request.getBook().getId()).orElseThrow();
        book.setLiked(book.getLiked() + 1);
        bookRepository.save(book);
        favoriteService.addFavorite(request.getCustomer(), request.getBook());
        return ResponseEntity.status(HttpStatus.CREATED).body("Favorite added");
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getFavorites(@PathVariable Long customerId) {
        return ResponseEntity.ok(favoriteService.getFavorites(customerId));
    }

    @GetMapping("/check")
    public ResponseEntity<Long> checkFavorite(
            @RequestParam("customerId") Long customerId,
            @RequestParam("bookId") Long bookId) {
        Long favoriteId = favoriteService.getFavoriteId(customerId, bookId);
        return ResponseEntity.ok(favoriteId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFavorite(@RequestParam("Id") Long favoriteId, @RequestParam("bookId") Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        book.setLiked(book.getLiked() - 1);
        favoriteService.deleteFavorite( favoriteId);
        return ResponseEntity.ok("Favorite deleted");
    }

}

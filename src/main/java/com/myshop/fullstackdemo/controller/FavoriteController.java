package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Favorite;
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
    @PostMapping("/new")
    public ResponseEntity<String> addFavorite(@RequestBody Favorite request) {
        favoriteService.addFavorite(request.getCustomer(), request.getBook());
        return ResponseEntity.status(HttpStatus.CREATED).body("Favorite added");
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getFavorites(@PathVariable Long customerId) {
        return ResponseEntity.ok(favoriteService.getFavorites(customerId));
    }

}

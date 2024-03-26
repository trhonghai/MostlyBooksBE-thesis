package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Category;
import com.myshop.fullstackdemo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    ResponseEntity<List<Category>> getAllCategories() {

        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/categories/new")
    ResponseEntity<Category> newCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.newCategory(category), HttpStatus.OK);
    }
}

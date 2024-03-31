package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.exception.NotFoundException;
import com.myshop.fullstackdemo.model.Category;
import com.myshop.fullstackdemo.repository.CategoryRepository;
import com.myshop.fullstackdemo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private  final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping
    ResponseEntity<List<Category>> getAllCategories() {

        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Category> getCategory(@PathVariable Long id) {
       return new ResponseEntity<>(categoryRepository.findById(Math.toIntExact(id)).orElseThrow(() -> new NotFoundException("Category not found, id=" + id)), HttpStatus.OK);
    }

    @PostMapping("/new")
    ResponseEntity<Category> newCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.newCategory(category), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Long id) {
        return new ResponseEntity<>(categoryService.updateCategory(category, id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(Math.toIntExact(id));
        return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
    }

}

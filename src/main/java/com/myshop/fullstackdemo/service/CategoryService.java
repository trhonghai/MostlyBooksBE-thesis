package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Category;
import com.myshop.fullstackdemo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public Category newCategory( Category category){
        category.setName(category.getName());
        return repo.save(category);
    }

    public Category updateCategory(Category category, Long id){
        Category categoryToUpdate = repo.findById(Math.toIntExact(id)).orElseThrow();
        categoryToUpdate.setName(category.getName());
        return repo.save(categoryToUpdate);
    }
    public List<Category> getAllCategories(){
        return repo.findAll();
    }
}

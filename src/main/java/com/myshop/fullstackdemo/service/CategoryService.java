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
    public List<Category> getAllCategories(){
        return repo.findAll();
    }
}

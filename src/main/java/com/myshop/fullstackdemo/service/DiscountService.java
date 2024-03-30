package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Discount;
public interface DiscountService {
    void applyDiscountToBook(Discount discount, Book book);
}

package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Discount;
import com.myshop.fullstackdemo.model.DiscountDetail;
import com.myshop.fullstackdemo.repository.DiscountDetailRepository;
import com.myshop.fullstackdemo.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService{

    @Autowired
    private  DiscountDetailRepository discountDetailRepository;
    @Autowired
    private  DiscountRepository discountRepository;

    @Override
    public void applyDiscountToBook(Discount discount, Book book) {
        DiscountDetail discountDetail = new DiscountDetail();
        discountDetail.setDiscount(discount);
        discountDetail.setBook(book);
        discountDetailRepository.save(discountDetail);
    }

    public void createDiscount(Discount discount, DiscountDetail discountDetail){
        discountRepository.save(discount);
        discountDetailRepository.save(discountDetail);
    }

}

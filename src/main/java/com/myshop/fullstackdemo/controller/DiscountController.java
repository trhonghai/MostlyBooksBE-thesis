package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Discount;
import com.myshop.fullstackdemo.model.DiscountDetail;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.DiscountDetailRepository;
import com.myshop.fullstackdemo.repository.DiscountRepository;
import com.myshop.fullstackdemo.service.DiscountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController()
@RequiredArgsConstructor
@RequestMapping(value = "/discount" )
public class DiscountController {

    private final DiscountServiceImpl discountService;
    private final DiscountRepository discountRepository;
    private final DiscountDetailRepository discountDetailRepository;
    private final BookRepository bookRepository;
    @PostMapping("/create/{bookId}")
    public ResponseEntity<Discount> createDiscount(@RequestBody DiscountRequest discountRequest, @PathVariable Long bookId) {
        System.out.println("Hi day la controller create discount");
        try{
            Discount discount = new Discount();
            discount.setName(discountRequest.getDiscountName());
            DiscountDetail discountDetail = new DiscountDetail();
            discountDetail.setDiscountPercentage(discountRequest.getDiscountPercentage());
            discountDetail.setStartDate(discountRequest.getStartDate());
            discountDetail.setEndDate(discountRequest.getEndDate());
            discountDetail.setDiscount(discount);

            Book book = bookRepository.findById(bookId).get();
            discountDetail.setBook(book);

            discountService.createDiscount(discount, discountDetail);
            return new ResponseEntity<>(discount, HttpStatus.CREATED);
        } catch ( Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DiscountDetail>> getDiscount(@PathVariable Long id){
        try{
            List<DiscountDetail> discountDetail = discountDetailRepository.findDiscountDetailByBookId(id);
            return new ResponseEntity<>(discountDetail, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}

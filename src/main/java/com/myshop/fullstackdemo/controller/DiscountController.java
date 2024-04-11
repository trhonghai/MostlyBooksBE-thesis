package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.dto.DiscountDTO;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController()
@RequiredArgsConstructor
@RequestMapping(value = "/discounts" )
public class DiscountController {

    private final DiscountServiceImpl discountService;
    private final DiscountRepository discountRepository;
    private final DiscountDetailRepository discountDetailRepository;
    private final BookRepository bookRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createDiscount(@RequestBody DiscountRequest discountRequest) {
        try {
            // Tạo đối tượng Discount từ dữ liệu trong DiscountRequest
            Discount discount = new Discount();
            discount.setName(discountRequest.getDiscountName());

            // Lấy danh sách các ID sách từ DiscountRequest
            List<Long> bookIds = discountRequest.getBookIds();

            // Tạo chiết khấu cho mỗi quyển sách trong danh sách bookIds
            for (Long bookId : bookIds) {
                // Tạo đối tượng DiscountDetail từ dữ liệu trong DiscountRequest
                DiscountDetail discountDetail = new DiscountDetail();
                discountDetail.setStartDate(discountRequest.getStartDate());
                discountDetail.setEndDate(discountRequest.getEndDate());
                discountDetail.setDiscountPercentage(discountRequest.getDiscountPercentage());
                discountDetail.setDiscount(discount);
                discountDetail.setDiscountCode(discountRequest.getDiscountCode());

                // Lấy thông tin của quyển sách từ bookRepository
                Book book = bookRepository.findById(bookId).orElse(null);
                if (book != null) {
                    discountDetail.setBook(book);
                    // Tạo chiết khấu với chi tiết đã được thiết lập
                    discountService.createDiscount(discount, discountDetail);
                }
            }

            return new ResponseEntity<>(discount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DiscountDetail>> getDiscount(@PathVariable Long id){
        try{
            DiscountDetail discountDetail = (DiscountDetail) discountDetailRepository.findDiscountDetailByBookId(id);
            return new  ResponseEntity<>(Collections.singletonList(discountDetail), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteDiscount(@PathVariable Long id) {
        try {
            discountDetailRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        List<DiscountDetail> discountDetails = discountDetailRepository.findAll();

        List<DiscountDTO> discountDTOs = discountDetails.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(discountDTOs, HttpStatus.OK);
    }

    private DiscountDTO convertToDTO(DiscountDetail discountDetail) {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setId(discountDetail.getId());
        discountDTO.setDiscountCode(discountDetail.getDiscountCode());
        discountDTO.setDiscountName(discountDetail.getDiscount().getName());
        discountDTO.setDiscountPercentage(discountDetail.getDiscountPercentage());
        discountDTO.setStartDate(discountDetail.getStartDate().toString());
        discountDTO.setEndDate(discountDetail.getEndDate().toString());
        discountDTO.setBook(discountDetail.getBook());
        // Thêm các thông tin khác cần thiết từ đối tượng DiscountDetail vào DiscountDTO

        return discountDTO;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DiscountDetail> updateDiscount(@PathVariable Long id, @RequestBody DiscountRequest discountRequest) {
        DiscountDetail discountDetail = discountDetailRepository.findById(id).orElse(null);
        if (discountDetail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Long> bookIds = discountRequest.getBookIds();
        for (Long bookId : bookIds) {
            // Tạo đối tượng DiscountDetail từ dữ liệu trong DiscountRequest
            discountDetail.setStartDate(discountRequest.getStartDate());
            discountDetail.setEndDate(discountRequest.getEndDate());
            discountDetail.setDiscountPercentage(discountRequest.getDiscountPercentage());
            discountDetail.setDiscountCode(discountRequest.getDiscountCode());
            discountDetail.setDiscountCode(discountRequest.getDiscountCode());

            // Lấy thông tin của quyển sách từ bookRepository
            Book book = bookRepository.findById(bookId).orElse(null);
            if (book != null) {
                discountDetail.setBook(book);
                // Tạo chiết khấu với chi tiết đã được thiết lập
            }
            discountDetailRepository.save(discountDetail);
            return new ResponseEntity<>(discountDetail, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

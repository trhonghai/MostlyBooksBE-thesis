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
            DiscountDetail discountDetail = discountDetailRepository.findDiscountDetailByBookId(id);
            return new ResponseEntity<>(List.of(discountDetail), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
//        List<DiscountDTO> discounts = discountService.getAllDiscounts();
//
//        return new ResponseEntity<>(discounts, HttpStatus.OK);
//    }

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
        discountDTO.setDiscountCode(discountDetail.getDiscountCode());
        discountDTO.setDiscountName(discountDetail.getDiscount().getName());
        discountDTO.setDiscountPercentage(discountDetail.getDiscountPercentage());
        discountDTO.setStartDate(discountDetail.getStartDate().toString());
        discountDTO.setEndDate(discountDetail.getEndDate().toString());
        discountDTO.setBookIds(Collections.singletonList(discountDetail.getBook().getId()));
        // Thêm các thông tin khác cần thiết từ đối tượng DiscountDetail vào DiscountDTO

        return discountDTO;
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<Discount> updateDiscount(@RequestBody DiscountRequest discountRequest, @PathVariable Long id){
//        try{
//            Discount discount = discountRepository.findById(id).get();
//            discount.setName(discountRequest.getDiscountName());
//            DiscountDetail discountDetail = discountDetailRepository.findDiscountDetailByDiscountId(id);
//            discountDetail.setDiscountPercentage(discountRequest.getDiscountPercentage());
//            discountDetail.setStartDate(discountRequest.getStartDate());
//            discountDetail.setEndDate(discountRequest.getEndDate());
//            discountDetail.setDiscount(discount);
//            discountService.updateDiscount(discount, discountDetail);
//            return new ResponseEntity<>(discount, HttpStatus.OK);
//        } catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }




}

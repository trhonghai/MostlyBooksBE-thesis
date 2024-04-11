package com.myshop.fullstackdemo.dto;

import com.myshop.fullstackdemo.model.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private Long id;
    private String discountName;
    private String discountCode;
    private double discountPercentage;
    private String startDate;
    private String endDate;
    private Book book;


}

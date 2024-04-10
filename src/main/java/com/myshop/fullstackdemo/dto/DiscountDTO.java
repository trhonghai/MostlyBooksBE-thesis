package com.myshop.fullstackdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDTO {
    private String discountName;
    private String discountCode;
    private double discountPercentage;
    private String startDate;
    private String endDate;
    private List<Long> bookIds;


}

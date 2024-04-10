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
public class BookDTO {
    private Book book;
    private double currentPrice;
}

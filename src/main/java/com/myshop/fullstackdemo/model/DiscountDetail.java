package com.myshop.fullstackdemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discount_details")
public class DiscountDetail {
        @Id
        @GeneratedValue
        private Long id;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private Date startDate;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        private Date endDate;
        private double discountPercentage;

        @ManyToOne
        @JoinColumn(name = "discount_id")
        private Discount discount;

        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;



}

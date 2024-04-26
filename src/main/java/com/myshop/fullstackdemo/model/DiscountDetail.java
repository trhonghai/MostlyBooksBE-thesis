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
        private String discountCode;

        @ManyToOne
        @JoinColumn(name = "discount_id")
        private Discount discount;

        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;

        public float getCurrentPrice(){
                if (startDate != null && endDate != null) {
                        Date currentDate = new Date();
                        if (currentDate.after(startDate) && currentDate.before(endDate)) {
                                // Nếu hiện tại nằm trong thời gian giảm giá, tính giá đã giảm
                                double discountedPrice = book.getOriginalPrice() * (1 - (discountPercentage / 100));
                                return (float) discountedPrice;
                        }
                }
                // Nếu không có hoặc đã hết thời gian giảm giá, trả về giá gốc
                return book.getOriginalPrice();
        }


}

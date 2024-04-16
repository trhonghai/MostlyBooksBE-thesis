package com.myshop.fullstackdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikedReviewRequest {
    public Long reviewId;
    public Long customerId;
}

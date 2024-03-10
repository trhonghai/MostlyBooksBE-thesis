package com.myshop.fullstackdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCheckoutRequest {
    private long customerId;
    private List<Long> orderDetailsId;
}

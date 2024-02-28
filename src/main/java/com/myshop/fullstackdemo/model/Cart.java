package com.myshop.fullstackdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "cart_items")
public class Cart {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<OrderDetail> orderDetail = new ArrayList<>();
}

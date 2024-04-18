package com.myshop.fullstackdemo.model;

import com.myshop.fullstackdemo.model.provinces.Province;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "shipping_rate")
public class ShippingRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price;
    private int day;

    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;
}

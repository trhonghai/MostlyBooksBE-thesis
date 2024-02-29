package com.myshop.fullstackdemo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private float price;
    private String img;
    private Long pages;
    private String ISBN_10;
    private String ISBN_13;
    private String dimensions;
    private String issue;
    private String cover;
    @Column(name = "weight_book", nullable = false)
    private int weight;
    private float rating;
    private int reviewCount;
    private Long inventory;

    @ManyToOne
    @JoinColumn(name = "authour_id")
    private Authour authour;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<DetailsImage> images;

    @OneToMany(mappedBy = "book")
    private List<Reviews> reviews;

}

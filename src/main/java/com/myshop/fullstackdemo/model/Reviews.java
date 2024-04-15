package com.myshop.fullstackdemo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Reviews")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private float rating;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;
    private int like;

    @ManyToOne
    @JoinColumn(name="book_id")

    private Book book;


    @ManyToOne
    @JoinColumn(name="parent_id")
    private Reviews reviews;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}

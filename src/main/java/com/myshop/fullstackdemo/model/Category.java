package com.myshop.fullstackdemo.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.Set;


@Data
//@EqualsAndHashCode(exclude = "parent")
@Entity
@Table(name = "categories")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String image;

        @ManyToOne
        @JoinColumn(name="parent_id")
        private Category parent;

        @OneToMany(mappedBy = "parent")
        private Set<Category> children ;

    }

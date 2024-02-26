package com.myshop.fullstackdemo.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@JsonDeserialize(using = RoleDeserializer.class)
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;


}

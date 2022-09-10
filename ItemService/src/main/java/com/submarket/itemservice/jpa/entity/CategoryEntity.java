package com.submarket.itemservice.jpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CATEGORY")
@Cacheable
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categorySeq;

    @Column(nullable = false, length = 300)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    List<ItemEntity> items;




}

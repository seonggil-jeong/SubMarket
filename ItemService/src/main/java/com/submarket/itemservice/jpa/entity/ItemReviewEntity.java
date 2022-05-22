package com.submarket.itemservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "itemReviewInfo")
@JsonIgnoreProperties({"item"})
public class ItemReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewSeq;

    @Column(nullable = false)
    private int userSeq;

    @Column(nullable = false)
    private int reviewStar;

    @Column(nullable = false)
    private String reviewContents;

    @Column(nullable = false)
    private String reviewDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private ItemEntity item;
}

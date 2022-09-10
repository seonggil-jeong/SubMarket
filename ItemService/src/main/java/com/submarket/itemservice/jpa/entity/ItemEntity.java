package com.submarket.itemservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "ITEM")
@ToString(exclude={"category"})
@Setter
@DynamicInsert
@DynamicUpdate
@Cacheable
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_seq")
    private Integer itemSeq;

    @Column(nullable = false)
    private String sellerId;

    @Column(length = 300, nullable = false)
    private String itemTitle;

    @Column(nullable = false)
    private int itemPrice;

    @Column(length = 2000)
    private String itemContents;

    @Column(nullable = false)
    private int itemCount;

    @Column(length = 300)
    private int readCount20;

    @Column(length = 300)
    private int readCount30;

    @Column(length = 300)
    private int readCount40;

    @Column(length = 300)
    private int readCountOther;

    @Column(nullable = false)
    private int itemStatus;

    @Column(nullable = false, length = 300) // Main 이미지는 Null 일 수 없음
    private String mainImagePath;

    @Column(nullable = true, length = 300)
    private String subImagePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private CategoryEntity category;


    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<ItemReviewEntity> reviews;

}

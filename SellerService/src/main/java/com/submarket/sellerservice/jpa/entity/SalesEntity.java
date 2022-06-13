package com.submarket.sellerservice.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Entity
@Table(name = "salesInfo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"seller"})
public class SalesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salesSeq;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private SellerEntity seller;
}



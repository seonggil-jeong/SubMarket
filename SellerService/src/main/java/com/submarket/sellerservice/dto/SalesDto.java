package com.submarket.sellerservice.dto;

import com.submarket.sellerservice.jpa.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {
    private int salesSeq;
    private String date;
    private int value;

    private SellerEntity seller;
}

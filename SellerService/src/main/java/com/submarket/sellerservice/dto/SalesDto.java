package com.submarket.sellerservice.dto;

import com.submarket.sellerservice.jpa.entity.SellerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {
    private int salesSeq;
    private String date;
    private int value;

    private SellerEntity seller;

    private List<SalesDto> response;
}

package com.submarket.sellerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerDto {
    private int sellerSeq;
    private String sellerId;
    private String sellerPassword;
    private String sellerEncPassword;
    private String businessId;
    private String sellerPn;
    private String sellerEmail;
    private String sellerAddress;
    private String sellerAddress2;
    private String sellerHome;
    private String sellerName;
    private int sellerStatus;
}

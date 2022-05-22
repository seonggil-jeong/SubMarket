package com.submarket.sellerservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseSellerInfo {
    private int sellerSeq;
    private String sellerId;
    private String businessId;
    private String sellerPn;
    private String sellerEmail;
    private String sellerAddress;
    private String sellerAddress2;
    private String sellerHome;
    private String sellerName;

}

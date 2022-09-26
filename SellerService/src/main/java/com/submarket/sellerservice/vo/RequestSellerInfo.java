package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSellerInfo {
    private int sellerSeq;
    private String sellerId;
    private String sellerPassword;
    private String businessId;
    private String sellerPn;
    private String sellerEmail;
    private String sellerAddress;
    private String sellerAddress2;
    private String sellerHome;
    private String sellerName;

}

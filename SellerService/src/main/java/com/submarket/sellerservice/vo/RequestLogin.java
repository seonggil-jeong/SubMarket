package com.submarket.sellerservice.vo;

import lombok.Data;

@Data
public class RequestLogin {
    private String sellerId;
    private String sellerPassword;
}

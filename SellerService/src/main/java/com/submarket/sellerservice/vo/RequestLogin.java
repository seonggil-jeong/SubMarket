package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestLogin {
    private String sellerId;
    private String sellerPassword;
}

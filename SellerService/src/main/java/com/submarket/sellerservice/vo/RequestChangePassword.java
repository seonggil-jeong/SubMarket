package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestChangePassword {
    private String oldPassword;
    private String newPassword;
}

package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "판매자 로그인")
public class RequestLogin {
    @Schema(description = "판매자 아이디", required = true, example = "sellerId")
    private String sellerId;
    @Schema(description = "판매자 비밀번호", required = true, example = "sellerPassword")
    private String sellerPassword;
}

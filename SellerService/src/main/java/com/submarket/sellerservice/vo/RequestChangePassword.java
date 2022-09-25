package com.submarket.sellerservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request - 판매자 비밀번호 변경")
public class RequestChangePassword {
    @Schema(description = "판매자 이전 비밀번호", required = true, example = "oldPassword")
    private String oldPassword;
    @Schema(description = "판매자 새로운 비밀번호", required = true, example = "newPassword")
    private String newPassword;
}

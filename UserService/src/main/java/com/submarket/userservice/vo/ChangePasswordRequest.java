package com.submarket.userservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request - 사용자 비밀번호 변경")
public class ChangePasswordRequest {
    @Schema(example = "oldPassword", required = true, description = "이전 비밀번호")
    private String oldPassword;
    @Schema(example = "oldPassword", required = true, description = "변경된 비밀번호")
    private String newPassword;
}

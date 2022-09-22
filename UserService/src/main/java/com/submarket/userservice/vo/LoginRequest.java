package com.submarket.userservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request - 로그인 요청 (token 생성)")
public class LoginRequest {
    @NotNull
    @Schema(required = true, description = "사용자 아이디", example = "userId")
    private String userId;

    @NotNull
    @Schema(required = true, description = "사용자 비밀번호", example = "userPassword")
    private String userPassword;
}

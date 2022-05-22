package com.submarket.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    @NotNull(message = "userId cannot be null")
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull
    private String userName;

    @Email(message = "이메일이 아닙니다.")
    private String userEmail;

    @NotNull
    private String userAge;

    @NotNull
    private String userPn;

    @NotNull
    private String userAddress;

    private String userAddress2;
}

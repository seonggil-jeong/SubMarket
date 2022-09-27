package com.submarket.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String userId;
    private String userPassword;
    private String userName;
    private String userEmail;
    private String userAge;
    private String userPn;
    private String userAddress;
    private String userAddress2;
}

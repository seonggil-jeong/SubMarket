package com.submarket.userservice.vo;

import lombok.Data;

@Data
public class RequestChangePassword {
    private String oldPassword;
    private String newPassword;
}

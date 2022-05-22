package com.submarket.userservice.vo;

import lombok.Data;

@Data
public class RequestLogin {
    private String userId;
    private String userPassword;
}

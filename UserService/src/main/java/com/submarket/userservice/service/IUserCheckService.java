package com.submarket.userservice.service;

public interface IUserCheckService {

    boolean checkUserByUserId(String userId);

    boolean checkUserByUserEmail(String userEmail);
}

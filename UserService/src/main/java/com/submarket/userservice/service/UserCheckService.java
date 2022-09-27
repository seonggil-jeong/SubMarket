package com.submarket.userservice.service;

public interface UserCheckService {

    boolean checkUserByUserId(String userId);

    boolean checkUserByUserEmail(String userEmail);

    boolean isTruePassword(String userId, String userPassword) throws Exception;
}

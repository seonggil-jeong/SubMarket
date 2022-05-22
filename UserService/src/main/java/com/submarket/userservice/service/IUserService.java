package com.submarket.userservice.service;

import com.submarket.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    int createUser(UserDto pDTO) throws Exception;

    UserDto getUserDetailsByUserId(String userId);

}

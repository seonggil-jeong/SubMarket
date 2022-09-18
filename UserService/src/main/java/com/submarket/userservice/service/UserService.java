package com.submarket.userservice.service;

import com.submarket.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    int createUser(UserDto pDTO) throws Exception;

    UserDto getUserInfoByUserEmail(String userEmail);

    int changeUserPassword(UserDto pDTO, String newPassword) throws Exception;

    int deleteUser(UserDto userDto) throws Exception;

    UserDto getUserDetailsByUserId(String userId);

    UserDto getUserInfoByUserId(String userId);

    int modifyUserInfo(UserDto userDto) throws Exception;

    int changeUserPasswordNoAuthorization(String userId, String newPassword) throws Exception;



}

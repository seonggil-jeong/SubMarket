package com.submarket.userservice.service.impl;

import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.service.IUserCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserCheckService implements IUserCheckService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**<-------------------------->If (UserEntity == null) ==> return 0 </-------------------------->*/
    @Override
    public boolean checkUserByUserId(String userId) {
        boolean check = false;
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) { // 중복 X 실행 가능
            check = true;
        }
        return check;
    }
}

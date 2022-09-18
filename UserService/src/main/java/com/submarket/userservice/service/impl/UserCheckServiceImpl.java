package com.submarket.userservice.service.impl;

import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserCheckServiceImpl implements com.submarket.userservice.service.UserCheckService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**<-------------------------->If (UserEntity == null) ==> return 0 </-------------------------->*/

    /**<--------------------------->아이디 중복 확인</---------------------------> */
    @Override
    public boolean checkUserByUserId(String userId) {
        boolean check = false;
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) { // 중복 X 실행 가능
            check = true;
        }
        return check;
    }

    /**<--------------------------->이메일 중복 확인</---------------------------> */
    @Override
    public boolean checkUserByUserEmail(String userEmail) {
        boolean check = false;
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);

        if (userEntity == null) { // 중복 X 실행 가능
            check = true;
        }

        return check;
    }

    /** <------------------------->비밀번호 일치 여부 확인</-------------------------> */
    @Override
    public boolean isTruePassword(String userId, String userPassword) throws Exception {
        log.info("--------------> " + this.getClass().getName() + "isTruePassword Start!");

        UserEntity userEntity = userRepository.findByUserId(userId);
        // 사용자 ID를 가지고 정보 불러오기
        if (userEntity != null) { // 비밀번호 일치 실행 가능
            log.info("Find User True");
            boolean check = passwordEncoder.matches(userPassword, userEntity.getUserPassword());
            // 사용자가 보낸 이전 Password 와 DB에 저장된 Password 를 비교
            return check;
        } else {
            return false;
        }
    }
}

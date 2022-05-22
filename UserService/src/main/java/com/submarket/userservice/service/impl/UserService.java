package com.submarket.userservice.service.impl;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.mapper.UserMapper;
import com.submarket.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service("UserService")
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCheckService userCheckService;
    private final MailService mailService;

    //####################################### 회원가입 #######################################//
    @Override
    public int createUser(UserDto pDTO) throws Exception {
        log.info("-------------->  " + this.getClass().getName() + ".createUser Start !");
        /** 아이디 중복 확인 (1 = 중복, 0 = pass)*/

        boolean checkId = userCheckService.checkUserByUserId(pDTO.getUserId());
        boolean checkEmail = userCheckService.checkUserByUserEmail(pDTO.getUserEmail());

        if (checkId && checkEmail) { /** ID or Email 에서 중복확인 완료 실행 가능 */ // 둘 다 0이 넘어와야지만 아래 코드 실행
            pDTO.setUserStatus(1); // 사용자 활성화 / (이메일 체크 후 활성화 로직 추가)
            pDTO.setUserEncPassword(passwordEncoder.encode(pDTO.getUserPassword()));
            UserEntity pEntity = UserMapper.INSTANCE.userDtoToUserEntity(pDTO);
            userRepository.save(pEntity);

            // 환영 메일 전송
            mailService.sendMail(pDTO.getUserEmail(), "Welcome!!", pDTO.getUserName() + "님 SubMarket 가입을 환영합니다!");


        } else { /** 중복 발생 실패 */
            return 0;
        }
        log.info("-------------->  " + this.getClass().getName() + ".createUser End !");
        return 1;
    }

    //####################################### 사용자 정보 조회 By UserEmail #######################################//
    @Override
    public UserDto getUserInfoByUserEmail(String userEmail) {
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);

        UserDto rDTO = UserMapper.INSTANCE.userEntityToUserDto(userEntity);

        return rDTO;
    }



    //####################################### JWT Don't change #######################################//
    @Override
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity rEntity = userRepository.findByUserId(userId);

        if (rEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        UserDto rDTO = new UserDto();
        rDTO.setUserId(rEntity.getUserId());
        rDTO.setUserPassword(rEntity.getUserPassword());

        return rDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        log.info("username : " + userId);
        UserEntity rEntity = userRepository.findByUserId(userId);

        if (rEntity == null) {
            throw new UsernameNotFoundException(userId);
        }

        return new User(rEntity.getUserId(), rEntity.getUserPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}

package com.submarket.userservice.service.impl;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.exception.UserException;
import com.submarket.userservice.exception.result.UserExceptionResult;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.mapper.UserMapper;
import com.submarket.userservice.service.UserService;
import com.submarket.userservice.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;


@Service("UserService")
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCheckServiceImpl userCheckServiceImpl;
    private final MailServiceImpl mailServiceImpl;


    //####################################### 회원가입 #######################################//
    @Override
    public int createUser(UserDto pDTO) throws Exception {
        log.info("-------------->  " + this.getClass().getName() + ".createUser Start !");
        /** 아이디 중복 확인 (1 = 중복, 0 = pass)*/

        boolean checkId = userCheckServiceImpl.checkUserByUserId(pDTO.getUserId());
        boolean checkEmail = userCheckServiceImpl.checkUserByUserEmail(pDTO.getUserEmail());


        if (checkId && checkEmail) { /** ID or Email 에서 중복확인 완료 실행 가능 */ // 둘 다 0이 넘어와야지만 아래 코드 실행
            pDTO.setUserStatus(1); // 사용자 활성화 / (이메일 체크 후 활성화 로직 추가)
            pDTO.setUserEncPassword(passwordEncoder.encode(pDTO.getUserPassword()));
            UserEntity pEntity = UserMapper.INSTANCE.userDtoToUserEntity(pDTO);
            userRepository.save(pEntity);

            // 환영 메일 전송
            mailServiceImpl.sendMail(pDTO.getUserEmail(), "Welcome!!", pDTO.getUserName() + "님 SubMarket 가입을 환영합니다!");


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

    //####################################### 사용자 정보 조회 By UserId #######################################//
    @Override
    @Transactional
    public UserDto getUserInfoByUserId(String userId) {
        log.info(this.getClass().getName() + ".getUserInfoByUser Start!");

        UserEntity userEntity = userRepository.findByUserId(userId);

        UserDto userDto = UserMapper.INSTANCE.userEntityToUserDto(userEntity);

        log.info(this.getClass().getName() + ".getUserInfoByUser End!");

        return userDto;
    }

    @Override
    public int changeUserPassword(UserDto pDTO, String newPassword) throws Exception {
        log.info(this.getClass().getName() + "changeUserPassword Start!");
        String userId = pDTO.getUserId();
        String userPassword = pDTO.getUserPassword();

        // 비밀번호가 일치하는지 확인
        boolean checkPassword = userCheckServiceImpl.isTruePassword(userId, userPassword);

        // 만약 비밀번호가 일치한다면
        if (checkPassword) {
            log.info("비밀번호 일치");
            userRepository.changeUserPassword(passwordEncoder.encode(newPassword), pDTO.getUserId());

            log.info(this.getClass().getName() + "changeUserPassword End!");
            return 1; // 성공
        }
        log.info(this.getClass().getName() + "changeUserPassword End!");
        return 0; // 실패



    }

    @Override
    public int changeUserPasswordNoAuthorization(String userId, String newPassword) throws Exception {
        log.info(this.getClass().getName() + "changeUserPasswordNoAuthorization Start!");

        userRepository.changeUserPassword(passwordEncoder.encode(newPassword), userId);

        log.info(this.getClass().getName() + "changeUserPasswordNoAuthorization End!");

        return 1;



    }

    @Override
    @Transactional
    public int deleteUser(UserDto userDto) throws Exception {
        log.info(this.getClass().getName() + ".deleteUser Start!");
        // 비밀번호 일치 확인
        if (userCheckServiceImpl.isTruePassword(userDto.getUserId(), userDto.getUserPassword())) {
            // 비밀번호가 일치한다면
            userRepository.deleteUserInfo(userDto.getUserId());
        } else {
            throw new UserException(UserExceptionResult.USER_PASSWORD_NOT_MATCHED);
        }


        log.info(this.getClass().getName() + ".deleteUser End!");

        return 0;
    }

    @Override // 사용자 정보 수정
    @Transactional
    public int modifyUserInfo(UserDto userDto) throws Exception {
        log.info(this.getClass().getName() + ".modifyUserInfo Start!");
        String userEmail = CmmUtil.nvl(userDto.getUserEmail());
        String userAddress = CmmUtil.nvl(userDto.getUserAddress());
        String userAddress2 = CmmUtil.nvl(userDto.getUserAddress2());
        String userId = CmmUtil.nvl(userDto.getUserId());
        String userAge = CmmUtil.nvl(userDto.getUserAge());
        String userPn = CmmUtil.nvl(userDto.getUserPn());

        log.info("userId : " + userId);
        log.info("userEmail : " + userEmail);

        userRepository.modifyUserInfo(userEmail, userAddress, userAddress2,userPn, userAge, userId);

        log.info(this.getClass().getName() + ".modifyUserInfo End!");
        return 1;
    }

    //####################################### JWT Don't change #######################################//
    @Override
    public UserDto getUserDetailsByUserId(String userId) {
        UserEntity rEntity = userRepository.findByUserId(userId);

        if (rEntity == null || rEntity.getUserStatus() == 0) {
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

        if (rEntity == null || rEntity.getUserStatus() == 0) {
            // 사용자 정보가 없거나 탈퇴한 사용자라면 (401)
            throw new UsernameNotFoundException(userId);
        }

        return new User(rEntity.getUserId(), rEntity.getUserPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}

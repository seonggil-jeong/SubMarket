package com.submarket.userservice.controller;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.mapper.UserMapper;
import com.submarket.userservice.service.impl.UserCheckService;
import com.submarket.userservice.service.impl.UserService;
import com.submarket.userservice.util.TokenUtil;
import com.submarket.userservice.vo.RequestChangePassword;
import com.submarket.userservice.vo.RequestUser;
import com.submarket.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserCheckService userCheckService;
    private final Environment env;
    private final TokenUtil tokenUtil;

    /**<---------------------->회원가입</---------------------->*/
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody RequestUser requestUser) throws Exception {
        log.info("-------------->  " + this.getClass().getName() + ".createUser Start!");
        int res = 0;

        UserDto pDTO = UserMapper.INSTANCE.RequestUserToUserDto(requestUser);

        res = userService.createUser(pDTO);

        if (res == 0) { /** 아이디 중복 발생 */
            return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디 또는 이메일을 확인해주세요."); // 충돌 발생
        }

        log.info("-------------->  " + this.getClass().getName() + ".createUser End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @GetMapping("/user")
    public ResponseEntity<ResponseUser> getUserInfo(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + "getUserInfo Start!");
        // 사용자 토큰을 사용하여 사용자 정보 조회
        String userId = tokenUtil.getUserIdByToken(headers);

        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        UserDto userDto = userService.getUserInfoByUserId(userId);

        ResponseUser responseUser = UserMapper.INSTANCE.UserDtoToResponseUser(userDto);

        log.info(this.getClass().getName() + "getUserInfo End!");
        return ResponseEntity.ok().body(responseUser);
    }

    /**<------------------------>아이디 중복 확인</------------------------>*/
    @GetMapping("/users/check-id/{userId}")
    public ResponseEntity<String> checkUserById(@PathVariable String userId) throws Exception {
        log.info("-------------------- > " + this.getClass().getName() + "checkId Start!");
        boolean checkId = userCheckService.checkUserByUserId(userId);

        if (checkId) {
            log.info("-------------------- > " + this.getClass().getName() + "checkId End!");
            return ResponseEntity.status(HttpStatus.OK).body("사용가능한 아이디 입니다");
        }
        log.info("아이디 중복 발생");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디 중복 입니다");
    }

    /**<------------------------>이메일 중복 확인</------------------------>*/
    @GetMapping("/users/check-email/{userEmail}")
    public ResponseEntity<String> checkUserByEmail(@PathVariable String userEmail) throws Exception {
        log.info("-------------------- > " + this.getClass().getSimpleName() + "checkEmail Start!");
        boolean checkEmail = userCheckService.checkUserByUserEmail(userEmail);

        if (checkEmail) {
            log.info("-------------------- > " + this.getClass().getName() + "checkEmail End!");
            return ResponseEntity.status(HttpStatus.OK).body("사용가능한 이메일 입니다.");

        }
        log.info("이메일 중복 발생");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일 중복 입니다");
    }


    /**<------------------------>아이디 찾기 with UserEmail </------------------------>
     * 만약 Email 이 같다면 아이디 정보 일부를 제공*/
    @GetMapping("/users/find-id/{userEmail}")
    public ResponseEntity<String> findUserId(@PathVariable String userEmail) throws Exception {
        log.info("-------------------- > " + this.getClass().getName() + "findUserId Start!");
        UserDto rDTO = userService.getUserInfoByUserEmail(userEmail);
        String userId;

        if (rDTO == null) { /** 유저 정보가 없을 경우 Not Found return */
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        /** 사용자 정보가 있을 경우 수정 후 전송 */
        userId = rDTO.getUserId().replaceAll("(?<=.{4}).", "*");
        log.info("-------------------- > " + this.getClass().getName() + "findUserId End!");
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    /**<------------------------>비밀번호 변경</------------------------>*/
    @PostMapping("/users/changePassword")
    public ResponseEntity<String> changePassword(@RequestHeader HttpHeaders headers,
                                                 @RequestBody RequestChangePassword request) throws Exception {
        String userId = tokenUtil.getUserIdByToken(headers);

        log.info("userId : " + userId);
        UserDto pDTO = new UserDto();
        pDTO.setUserId(userId);
        pDTO.setUserPassword(request.getOldPassword());

        int check = userService.changeUserPassword(pDTO, request.getNewPassword());

        if (check == 1) { // 변경 성공
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이전 비밀번호를 확인해 주세요");

    }

    @PostMapping("/user/modify")
    public ResponseEntity<String> modifyUserInfo(@RequestHeader HttpHeaders headers, @RequestBody UserDto body)
        throws Exception {
        log.info(this.getClass().getName() + ".modifyUserInfo Start!");
        String userId = tokenUtil.getUserIdByToken(headers);
        log.info("userEmail in Controller : " + body.getUserEmail());

        body.setUserId(userId);

        int res = userService.modifyUserInfo(body);
        if (res == 1) {
            return ResponseEntity.ok().body("변경 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestHeader HttpHeaders headers, @RequestBody RequestUser requestUser) throws Exception {
        /**
         * 비밀번호가 일치한다면
         * 사용자 Status 0으로 변경
         */
        log.info(this.getClass().getName() + ".deleteUser Start!");

        // GetUserId from Token
        String userId = tokenUtil.getUserIdByToken(headers);
        UserDto pDto = new UserDto();
        pDto.setUserPassword(requestUser.getUserPassword());
        pDto.setUserId(userId);

        // TODO: 2022-05-23 요청
        userService.deleteUser(pDto);

        log.info(this.getClass().getName() + ".deleteUser Start!");

        return null;
    }
}

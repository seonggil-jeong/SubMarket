package com.submarket.userservice.controller;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.service.impl.UserCheckService;
import com.submarket.userservice.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserCheckService userCheckService;
    private final Environment env;


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
}

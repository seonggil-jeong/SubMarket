package com.submarket.userservice.controller;

import com.submarket.userservice.dto.ItemDto;
import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.mapper.UserMapper;
import com.submarket.userservice.service.UserCheckService;
import com.submarket.userservice.service.UserItemService;
import com.submarket.userservice.service.UserService;
import com.submarket.userservice.service.impl.MailServiceImpl;
import com.submarket.userservice.service.impl.UserCheckServiceImpl;
import com.submarket.userservice.service.impl.UserServiceImpl;
import com.submarket.userservice.util.CmmUtil;
import com.submarket.userservice.util.TokenUtil;
import com.submarket.userservice.vo.ItemLikedRequest;
import com.submarket.userservice.vo.ChangePasswordRequest;
import com.submarket.userservice.vo.UserRequest;
import com.submarket.userservice.vo.UserResponse;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "사용자 API", description = "사용자 API")
public class UserController {
    private final UserService userServiceImpl;
    private final UserCheckService userCheckServiceImpl;
    private final TokenUtil tokenUtil;
    private final MailServiceImpl mailServiceImpl;

    private final UserItemService userItemService;

    /**
     * <---------------------->회원가입</---------------------->
     */
    @Operation(summary = "사용자 회원가입", description = "사용자 회원가입 (인증 X)", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자 회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "사용자 아이디 또는 이메일 중복 발생")
    })
    @PostMapping("/users")
    @Timed(value = "user.createUser", longTask = true)
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) throws Exception {
        log.info("-------------->  " + this.getClass().getName() + ".createUser Start!");
        int res = 0;

        if (!userCheckServiceImpl.checkUserByUserId(userRequest.getUserId())) {
            // 아이디 중복
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 아이디 입니다.");
        }

        UserDto pDTO = UserMapper.INSTANCE.RequestUserToUserDto(userRequest);

        res = userServiceImpl.createUser(pDTO);

        if (res == 0) { /** 아이디 중복 발생 */
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 이메일 입니다"); // 충돌 발생
        }

        log.info("-------------->  " + this.getClass().getName() + ".createUser End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    @Operation(summary = "사용자 정보 조회", description = "토큰 정보를 사용하여 사용자 정보 조회", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공")
    })
    @GetMapping("/users")
    @Timed(value = "user.getUser", longTask = true)
    public ResponseEntity<UserResponse> getUserInfo(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + "getUserInfo Start!");
        // 사용자 토큰을 사용하여 사용자 정보 조회
        String userId = tokenUtil.getUserIdByToken(headers);

        if (userId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        UserDto userDto = userServiceImpl.getUserInfoByUserId(userId);

        UserResponse userResponse = UserMapper.INSTANCE.UserDtoToResponseUser(userDto);

        log.info(this.getClass().getName() + "getUserInfo End!");
        return ResponseEntity.ok().body(userResponse);
    }
    @Operation(summary = "사용자 아이디 중복 확인", description = "회원가입 전 사용자 아이디 중복 확인", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "409", description = "중복된 아이디")
    })
    /**
     * <------------------------>아이디 중복 확인</------------------------>
     */
    @GetMapping("/users/check-id/{userId}")
    @Timed(value = "user.check.id", longTask = true)
    public ResponseEntity<String> checkUserById(@PathVariable String userId) throws Exception {
        log.info("-------------------- > " + this.getClass().getName() + "checkId Start!");
        boolean checkId = userCheckServiceImpl.checkUserByUserId(userId);

        if (checkId) {
            log.info("-------------------- > " + this.getClass().getName() + "checkId End!");
            return ResponseEntity.status(HttpStatus.OK).body("사용가능한 아이디 입니다");
        }
        log.info("아이디 중복 발생");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디 중복 입니다");
    }

    @Operation(summary = "사용자 이메일 중복 확인", description = "회원가입 전 사용자 이메일 중복 확인", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
            @ApiResponse(responseCode = "409", description = "중복된 이메일")
    })
    /**
     * <------------------------>이메일 중복 확인</------------------------>
     */
    @GetMapping("/users/check-email/{userEmail}")
    @Timed(value = "user.check.email", longTask = true)
    public ResponseEntity<String> checkUserByEmail(@PathVariable String userEmail) throws Exception {
        log.info("-------------------- > " + this.getClass().getSimpleName() + "checkEmail Start!");
        boolean checkEmail = userCheckServiceImpl.checkUserByUserEmail(userEmail);

        if (checkEmail) {
            log.info("-------------------- > " + this.getClass().getName() + "checkEmail End!");
            return ResponseEntity.status(HttpStatus.OK).body("사용가능한 이메일 입니다.");

        }
        log.info("이메일 중복 발생");
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이메일 중복 입니다");
    }


    /**
     * <------------------------>아이디 찾기 with UserEmail </------------------------>
     * 만약 Email 이 같다면 아이디 정보 일부를 제공
     */
    @Operation(summary = "사용자 아이디 찾기", description = "Email을 기반으로 사용자 아이디 찾기", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용 가능한 아이디"),
            @ApiResponse(responseCode = "404", description = "사용자 정보를 찾을 수 없음")
    })
    @GetMapping("/users/find-id/{userEmail}")
    @Timed(value = "user.find.id", longTask = true)
    public ResponseEntity<String> findUserId(@PathVariable String userEmail) throws Exception {
        log.info("-------------------- > " + this.getClass().getName() + "findUserId Start!");
        UserDto rDTO = userServiceImpl.getUserInfoByUserEmail(userEmail);
        String userId;

        if (rDTO == null) { /** 유저 정보가 없을 경우 Not Found return */
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        /** 사용자 정보가 있을 경우 수정 후 전송 */
        userId = rDTO.getUserId().replaceAll("(?<=.{4}).", "*");
        log.info("-------------------- > " + this.getClass().getName() + "findUserId End!");
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    /**
     * <------------------------>비밀번호 변경</------------------------>
     */
    @Operation(summary = "사용자 비밀번호 변경", description = "이전 비밀번호 일치 시 비밀번호 변경", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "이전 비밀번호가 일치하지 않습니다")
    })
    @PostMapping("/users/changePassword")
    @Timed(value = "user.change.password", longTask = true)
    public ResponseEntity<String> changePassword(@RequestHeader HttpHeaders headers,
                                                 @RequestBody ChangePasswordRequest request) throws Exception {
        String userId = tokenUtil.getUserIdByToken(headers);

        log.info("userId : " + userId);
        UserDto pDTO = new UserDto();
        pDTO.setUserId(userId);
        pDTO.setUserPassword(request.getOldPassword());

        int check = userServiceImpl.changeUserPassword(pDTO, request.getNewPassword());

        if (check == 1) { // 변경 성공
            return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이전 비밀번호를 확인해 주세요");

    }

    @Operation(summary = "사용자 정보 변경", description = "사용자 정보를 변경", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "사용자 정보 변경 실패")
    })
    @PostMapping("/users/modify")
    @Timed(value = "user.change.userInfo", longTask = true)
    public ResponseEntity<String> modifyUserInfo(@RequestHeader HttpHeaders headers, @RequestBody UserDto body)
            throws Exception {
        log.info(this.getClass().getName() + ".modifyUserInfo Start!");
        String userId = tokenUtil.getUserIdByToken(headers);
        log.info("userEmail in Controller : " + body.getUserEmail());

        body.setUserId(userId);

        int res = userServiceImpl.modifyUserInfo(body);
        if (res == 1) {
            return ResponseEntity.ok().body("변경 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error");
        }
    }


    @Operation(summary = "사용자 비밀번호 찾기", description = "인증 후 사용자 임시 비밀번호를 Mail로 전송", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email 로 임시 비밀번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 정보가 일치하지 않음"),
            @ApiResponse(responseCode = "400", description = "아이디로 사용자 정보를 찾을 수 없음")
    })
    @PostMapping("/users/fix/find-password")
    @Timed(value = "user.find.password", longTask = true)
    public ResponseEntity<String> findPassword(@RequestBody UserDto userDto) throws Exception {
        log.info(this.getClass().getName() + ".findPassword Start");
        String userId = CmmUtil.nvl(userDto.getUserId());
        String userEmail = CmmUtil.nvl(userDto.getUserEmail());
        if (!userCheckServiceImpl.checkUserByUserId(userId)) {
            // 아이디가 중복 = DB 에 있음
            UserDto checkDto = userServiceImpl.getUserInfoByUserId(userId);
            if (checkDto.getUserEmail().equals(userEmail)) {
                // Id로 조회한 Email in DB 값과 넘어온 값이 같으면 Mail 전송


                String exPassword = String.valueOf(UUID.randomUUID());
                userServiceImpl.changeUserPasswordNoAuthorization(userId, exPassword);

                mailServiceImpl.sendMail(userDto.getUserEmail(), "비밀번호 변경", "임시 비밀번호 : " + exPassword + "입니다.");


            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 정보를 확인해 주세요");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디와 일치하는 정보가 없습니다");
        }

        log.info(this.getClass().getName() + ".findPassword End");

        return ResponseEntity.ok().body(userEmail + "로 임시 비밀번호를 발송 했습니다");
    }


    @Operation(summary = "사용자 삭제 (비활성화)", description = "인증 후 사용자 정보를 비활성화", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 비활성화 (탈퇴) 성공"),
            @ApiResponse(responseCode = "400", description = "이메일 정보가 일치하지 않음"),
            @ApiResponse(responseCode = "400", description = "사용자 비밀번호가 일치하지 않음")
    })
    @PostMapping("/users/delete")
    @Timed(value = "user.deleteUser", longTask = true)
    public ResponseEntity<String> deleteUser(@RequestHeader HttpHeaders headers, @RequestBody UserRequest userRequest) throws Exception {
        /**
         * 비밀번호가 일치한다면
         * 사용자 Status 0으로 변경
         */
        log.info(this.getClass().getName() + ".deleteUser Start!");

        // GetUserId from Token
        String userId = tokenUtil.getUserIdByToken(headers);
        UserDto pDto = new UserDto();
        pDto.setUserPassword(userRequest.getUserPassword());
        pDto.setUserId(userId);

        userServiceImpl.deleteUser(pDto);

        log.info(this.getClass().getName() + ".deleteUser Start!");

        return ResponseEntity.ok().body("회원탈퇴 완료");
    }

    /**
     * 좋아요 생성 or 삭제 (이미 있다면 삭제)
     *
     * @param headers Token
     * @param request itemSeq
     * @return String 결과
     * @throws Exception
     */
    @Operation(summary = "사용자 상품 좋아요", description = "사용자 상품 좋아요", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 좋아요 성공"),
            @ApiResponse(responseCode = "200", description = "이미 좋아요를 누른 상태라면, 좋아요 취소"),
            @ApiResponse(responseCode = "400", description = "상품 번호와 일치하는 상품 정보가 없음")
    })
    @PostMapping("/users/items/liked")
    @Timed(value = "user.like", longTask = true)
    public ResponseEntity<String> itemLiked(@RequestHeader final HttpHeaders headers,
                                            @RequestBody @Validated final ItemLikedRequest request) throws Exception {

        final String userId = tokenUtil.getUserIdByToken(headers);

        log.debug("userId : " + userId);

        return ResponseEntity.ok().body(userItemService.itemLikedOrDelete(userId, request.getItemSeq()));
    }

    @Operation(summary = "사용자 상품 좋아요 확인", description = "사용자 상품 좋아요 유무 확인", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 상품 좋아요 유무 확인 성공")
    })
    @GetMapping("/users/{userId}/items/{itemSeq}/liked")
    @Timed(value = "user.like", longTask = true)
    public ResponseEntity<Integer> isItemLiked(@PathVariable int itemSeq, @PathVariable String userId) throws Exception {
        log.info("isItemLiked Start!");

        log.info("테스트 로그 사용자 상품 좋아요 확인하기");
        final int result = userItemService.likedItemByUserId(userId, itemSeq);

        log.info("result : " + result);

        log.info("isItemLiked Start! ");


        return ResponseEntity.ok().body(result);

    }


    @Operation(summary = "좋아요한 상품 목록 조회", description = "사용자가 좋아요한 상품 정보 조회", tags = {"user", "item"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요한 상품 목록 조회 성공")
    })
    @GetMapping("/users/items/liked")
    public ResponseEntity<Map<String, Object>> findAllLikedItems(@RequestHeader final HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".findAllLikedItems Start!");

        Map<String, Object> responseBody = new HashMap<>();


        List<ItemDto> result = userItemService.findAllLikedItems(tokenUtil.getUserIdByToken(headers));

        responseBody.put("userId", tokenUtil.getUserIdByToken(headers));
        responseBody.put("response", result);


        log.info(this.getClass().getName() + ".findAllLikedItems End!");


        return ResponseEntity.ok().body(responseBody);
    }

}

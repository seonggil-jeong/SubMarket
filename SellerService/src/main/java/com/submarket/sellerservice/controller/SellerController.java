package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.mapper.SellerMapper;
import com.submarket.sellerservice.service.impl.SellerCheckService;
import com.submarket.sellerservice.service.impl.SellerService;
import com.submarket.sellerservice.util.TokenUtil;
import com.submarket.sellerservice.vo.RequestChangePassword;
import com.submarket.sellerservice.vo.RequestSellerInfo;
import com.submarket.sellerservice.vo.ResponseSellerInfo;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "판매자 정보 API", description = "판매자 정보 관련 API")
public class SellerController {
    private final SellerService sellerService;
    private final TokenUtil tokenUtil;
    private final SellerCheckService sellerCheckService;


    @Operation(summary = "판매자 정보 조회", description = "Token 값을 사용하여 사용자 정보 조회", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매출 정보 조회 성공")
    })
    @GetMapping("/seller")
    @Timed(value = "seller.findById", longTask = true)
    public ResponseEntity<ResponseSellerInfo> getSellerInfo(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".getSellerInfo Start!");
        SellerDto pDto = new SellerDto();

        String sellerId = tokenUtil.getUserIdByToken(headers);
        pDto.setSellerId(sellerId);

        SellerDto sellerDto = sellerService.getSellerInfoBySellerId(pDto);

        log.info("seller Service End!");

        ResponseSellerInfo sellerInfo = SellerMapper.INSTANCE.SellerDtoToResponseSellerInfo(sellerDto);

        if (sellerInfo == null) {
            log.info(this.getClass().getName() + "userToken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        log.info(this.getClass().getName() + ".getSellerInfo End!");

        return ResponseEntity.ok().body(sellerInfo);
    }


    @Operation(summary = "판매자 정보 등록", description = "판매자 정보 등록", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "중복된 아이디"),
            @ApiResponse(responseCode = "409", description = "중복된 이메일"),
            @ApiResponse(responseCode = "400", description = "회원가입 실패")
    })
    @PostMapping("/sellers")
    @Timed(value = "seller.save", longTask = true)
    public ResponseEntity<String> createSeller(@RequestBody RequestSellerInfo sellerInfo) throws Exception {
        log.info(this.getClass().getName() + ".createSeller Start!");

        SellerDto SellerDto = SellerMapper.INSTANCE.requestSellerInfoToSellerDto(sellerInfo);

        int res = sellerService.createSeller(SellerDto);

        if (res == 1) {
            // 회원가입 성공
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } else if (res == 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 이메일 입니다");
        } else if (res == 3) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 아이디 입니다");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }


    @Operation(summary = "판매자 정보 수정", description = "판매자 정보 수정", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 정보 수정 성공"),
            @ApiResponse(responseCode = "500", description = "판매자 정보 수정 실패")
    })
    @PostMapping("/sellers/modify")
    @Timed(value = "seller.modify", longTask = true)
    public ResponseEntity<String> modifySellerInfo(@RequestBody  SellerDto sellerDto,
                                                   @RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".modifySellerInfo Start!");

        String sellerId = tokenUtil.getUserIdByToken(headers);
        sellerDto.setSellerId(sellerId);

        int res = sellerService.modifySellerInfo(sellerDto);

        if (res == 1) {
            log.info("수정 완료");
            return ResponseEntity.ok().body("수정 성공");
        }

        log.info("수정 실패");
        log.info(this.getClass().getName() + ".modifySellerInfo End!");
        return ResponseEntity.ok().body("수정 실패 (500)");
    }


    @Operation(summary = "판매자 정보 삭제", description = "비밀번호 인증 후 판매자 정보 삭제", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 정보 삭제 성공")
    })
    @PostMapping("/sellers/drop")
    @Timed(value = "seller.drop", longTask = true)
    public ResponseEntity<String> deleteSeller(@RequestHeader HttpHeaders headers,
                                               @RequestBody RequestSellerInfo requestSellerInfo) throws Exception {
        log.info(this.getClass().getName() + ".deleteSeller Start!");
        SellerDto SellerDto = new SellerDto();

        String sellerId = tokenUtil.getUserIdByToken(headers);

        SellerDto.setSellerId(sellerId);
        SellerDto.setSellerPassword(requestSellerInfo.getSellerPassword());
        sellerService.deleteSeller(SellerDto);

        log.info(this.getClass().getName() + ".deleteSeller End!");


        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴 완료");
    }

    /**
     * <------------------------>아이디 찾기 with UserEmail </------------------------>
     * 만약 Email 이 같다면 아이디 정보 일부를 제공
     */
    @Operation(summary = "판매자 아이디 찾기", description = "이메일 인증 후 아이디 찾기", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 아이디 정보 성공"),
            @ApiResponse(responseCode = "404", description = "이메일과 일치하는 판매자 정보를 찾을 수 없음"),
    })
    @GetMapping("/sellers/find-id/{sellerEmail}")
    @Timed(value = "seller.find.id", longTask = true)
    public ResponseEntity<String> findSellerId(@PathVariable String sellerEmail) throws Exception {
        log.info("-------------------- > " + this.getClass().getName() + "findSellerId Start!");
        SellerDto sellerDto = new SellerDto();

        sellerDto.setSellerEmail(sellerEmail);

        sellerDto = sellerService.getSellerInfoBySellerEmail(sellerDto);

        if (sellerDto == null) { /** 유저 정보가 없을 경우 Not Found return */
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }
        /** 사용자 정보가 있을 경우 수정 후 전송 */
        String sellerId = sellerDto.getSellerId().replaceAll("(?<=.{4}).", "*");
        log.info("-------------------- > " + this.getClass().getName() + "findUserId End!");
        return ResponseEntity.status(HttpStatus.OK).body(sellerId);
    }


    @Operation(summary = "판매자 비밀번호 변경", description = "이전 비밀번호가 일치한다면 비밀번호 변경 진행", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 비밀번호 변경 성공")
    })
    @PatchMapping("/sellers/change-password")
    @Timed(value = "seller.change.password", longTask = true)
    public ResponseEntity<String> changePassword(@RequestHeader HttpHeaders headers, @RequestBody RequestChangePassword
            requestChangePassword) throws Exception {
        log.info(this.getClass().getName() + ".changePassword Start!");
        int res = 0;
        String sellerId = tokenUtil.getUserIdByToken(headers);

        res = sellerService.changePassword(requestChangePassword.getOldPassword(),
                requestChangePassword.getNewPassword(),
                sellerId);

        if (res == 0) {
            log.info("비밀번호 변경 실패");
            throw new RuntimeException("비밀번호 변경 실패");
        }

        log.info(this.getClass().getName() + ".changePassword End!");
        return ResponseEntity.ok().body("비밀번호 변경 성공");
    }


    @Operation(summary = "사업자 번호 유효성 검사", description = "Open API를 이용하여 정보가 있다면 OK", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유효한 사업자"),
            @ApiResponse(responseCode = "400", description = "유효하지 않는 사업자 번호")
    })
    // 사업자 번호 유효성 검사
    @GetMapping("/seller/business/{businessId}")
    @Timed(value = "seller.check.businessId", longTask = true)
    public ResponseEntity<Map<String, Object>> checkBusinessId(@PathVariable String businessId) throws Exception {
        log.info(this.getClass().getName() + ".checkBusinessId Start!");

        Map<String, Object> taxType = sellerCheckService.checkBusinessId(businessId);

        log.info(this.getClass().getName() + ".checkBusinessId End!");
        return ResponseEntity.ok().body(taxType);
    }
}


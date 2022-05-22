package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.mapper.SellerMapper;
import com.submarket.sellerservice.service.impl.SellerService;
import com.submarket.sellerservice.vo.RequestSellerInfo;
import com.submarket.sellerservice.vo.ResponseSellerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("/sellers")
    public ResponseEntity<String> createSeller(@RequestBody RequestSellerInfo sellerInfo) throws Exception{
        log.info(this.getClass().getName() + ".createSeller Start!");

        SellerDto SellerDto = SellerMapper.INSTANCE.requestSellerInfoToSellerDto(sellerInfo);
        log.info("sellerPassword : " + SellerDto.getSellerPassword());

        int res = sellerService.createSeller(SellerDto);

        if (res == 1) {
            // 회원가입 성공
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }
    @PostMapping("/sellers/drop")
    public ResponseEntity<String> deleteSeller(@RequestBody RequestSellerInfo requestSellerInfo) throws Exception {
        log.info(this.getClass().getName() + ".deleteSeller Start!");
        SellerDto SellerDto = new SellerDto();

        SellerDto.setSellerId(requestSellerInfo.getSellerId());
        SellerDto.setSellerPassword(requestSellerInfo.getSellerPassword());
        sellerService.deleteSeller(SellerDto);

        log.info(this.getClass().getName() + ".deleteSeller End!");


        return ResponseEntity.status(HttpStatus.OK).body("회원 탈퇴 완료");
    }
}


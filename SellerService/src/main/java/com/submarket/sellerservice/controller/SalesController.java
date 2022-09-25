package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.service.impl.SalesService;
import com.submarket.sellerservice.util.TokenUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "판매자 매출 정보 API", description = "사용자 매출 정보 관련 APi")
public class SalesController {
    private final SalesService salesService;
    private final TokenUtil tokenUtil;

    @Operation(summary = "모든 매출 정보 조회", description = "판매자 아이디를 기준으로 매출 목록 조회", tags = {"sales"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "매출 정보 조회 성공")
    })
    @GetMapping("/sellers/sales")
    @Timed(value = "seller.sales.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findAllSalesDtoBySellerId(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".findAllSalesDtoBySellerId Start!");
        String sellerId = tokenUtil.getUserIdByToken(headers);

        Map<String, Object> rMap = new HashMap<>();

        List<SalesDto> salesDtoList = salesService.findAllSalesDtoBySellerId(sellerId);

        rMap.put("response", salesDtoList);


        log.info(this.getClass().getName() + ".findAllSalesDtoBySellerId End!");

        return ResponseEntity.status(HttpStatus.OK).body(rMap);


    }
}

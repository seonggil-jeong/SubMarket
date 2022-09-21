package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.service.impl.SalesService;
import com.submarket.sellerservice.util.TokenUtil;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SalesController {
    private final SalesService salesService;
    private final TokenUtil tokenUtil;

    @GetMapping("/seller/sales")
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

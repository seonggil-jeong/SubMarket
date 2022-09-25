package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemServiceImpl;
import com.submarket.itemservice.util.TokenUtil;
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
@Tag(name = "Item Seller API", description = "판매자가 자신의 상품 정보를 관리하는 API")
public class SellerItemController {
    private final ItemServiceImpl itemService;
    private final TokenUtil tokenUtil;



    @Operation(summary = "판매자가 판매중인 상품 목록 조회", description = "판매자가 판매중인 상품 조회", tags = {"seller", "item"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 상품 조회 성공")
    })
    @GetMapping("/item/seller")
    @Timed(value = "seller.item.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findItemInfoBySellerId(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + "findItemInfoBySellerId Start!");
        String sellerId = tokenUtil.getUserIdByToken(headers);
        Map<String, Object> rMap = new HashMap<>();

        List<ItemDto> itemDtoList = itemService.findItemBySellerId(sellerId);

        rMap.put("response", itemDtoList);

        log.info(this.getClass().getName() + "findItemInfoBySellerId End!");
        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }
}

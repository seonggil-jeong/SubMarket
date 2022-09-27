package com.submarket.orderservice.controller;

import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.impl.OrderServiceImpl;
import com.submarket.orderservice.vo.OrderRequest;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;


    @Operation(summary = "주문 생성", description = "주문 생성", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 생성 완료")
    })
    @PostMapping("/orders")
    @Timed(value = "order.save", longTask = true)
    public ResponseEntity<String> insertOrder(@RequestBody OrderRequest request) throws Exception {
        log.info(this.getClass().getName() + ".insertOrder Start!");

        orderServiceImpl.insertOrder(OrderDto.builder()
                .orderId(request.getOrderId())
                .itemSeq(request.getItemSeq())
                .userId(request.getUserId())
                .userAddress(request.getUserAddress())
                .userAddress2(request.getUserAddress2())
                .sellerId(request.getSellerId())
                .orderDate(request.getOrderDate())
                .orderDateDetails(request.getOrderDateDetails()).build());


        log.info(this.getClass().getName() + ".insertOrder End!");
        return ResponseEntity.status(HttpStatus.CREATED).body("주문 생성 완료");
    }


    @Operation(summary = "사용자 주문 목록 조회", description = "사용자 아이디를 사용하여 주문 목록 조회", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    })
    @GetMapping("/users/{userId}/orders")
    @Timed(value = "user.order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoByUserId(@PathVariable String userId) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByUserId Start!");
        Map<String, Object> rMap = new HashMap<>();

        List<OrderDto> orderDtoList = orderServiceImpl.findAllOrderByUserId(userId);

        rMap.put("response", orderDtoList);


        log.info(this.getClass().getName() + ".findOrderInfoByUserId End!");


        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }


    @Operation(summary = "판매자 주문 목록 조회", description = "판매자 아이디를 사용하여 주문 목록 조회", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    })
    @GetMapping("sellers/{sellerId}/orders")
    @Timed(value = "seller.order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoBySellerId(@PathVariable String sellerId)
            throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoBySellerId Start!");

        Map<String, Object> rMap = new HashMap<>();

        List<OrderDto> orderDtoList = orderServiceImpl.findAllOrderBySellerId(sellerId);

        rMap.put("response", orderDtoList);


        log.info(this.getClass().getName() + ".findOrderInfoBySellerId End!");


        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }


    @Operation(summary = "주문 정보 상세 조회", description = "주문 번호를 사용하여 주문 번호 상세 조회", tags = {"seller", "user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공")
    })
    @GetMapping("/orders/{orderId}")
    @Timed(value = "order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoByOrderId(@PathVariable String orderId) throws Exception {
        Map<String, Object> rMap = new HashMap<>();

        OrderDto orderDto = orderServiceImpl.findOneOrderByOrderId(orderId);

        rMap.put("response", orderDto);

        if (orderDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(rMap);
        }


        return ResponseEntity.ok().body(rMap);
    }
}

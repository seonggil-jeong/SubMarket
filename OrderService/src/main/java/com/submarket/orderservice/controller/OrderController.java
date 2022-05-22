package com.submarket.orderservice.controller;

import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;


    @PostMapping("/order")
    public ResponseEntity<String> insertOrder(@RequestBody OrderDto orderDto) throws Exception {
        log.info(this.getClass().getName() + ".insertOrder Start!");

        orderService.insertOrder(orderDto);

        log.info(this.getClass().getName() + ".insertOrder End!");
        return ResponseEntity.status(HttpStatus.CREATED).body("주문 생성 완료");
    }
}

package com.submarket.orderservice.controller;

import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.impl.OrderService;
import io.micrometer.core.annotation.Timed;
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
    private final OrderService orderService;


    @PostMapping("/order")
    @Timed(value = "order.save", longTask = true)
    public ResponseEntity<String> insertOrder(@RequestBody OrderDto orderDto) throws Exception {
        log.info(this.getClass().getName() + ".insertOrder Start!");

        orderService.insertOrder(orderDto);

        log.info(this.getClass().getName() + ".insertOrder End!");
        return ResponseEntity.status(HttpStatus.CREATED).body("주문 생성 완료");
    }

    @GetMapping("/order/user/{userId}")
    @Timed(value = "user.order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoByUserId(@PathVariable String  userId) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByUserId Start!");
        Map<String, Object> rMap = new HashMap<>();

        List<OrderDto> orderDtoList = orderService.findAllOrderByUserId(userId);

        rMap.put("response", orderDtoList);


        log.info(this.getClass().getName() + ".findOrderInfoByUserId End!");


        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }

    @GetMapping("/order/seller/{sellerId}")
    @Timed(value = "seller.order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoBySellerId(@PathVariable String sellerId)
        throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoBySellerId Start!");

        Map<String, Object> rMap = new HashMap<>();

        List<OrderDto> orderDtoList = orderService.findAllOrderBySellerId(sellerId);

        rMap.put("response", orderDtoList);


        log.info(this.getClass().getName() + ".findOrderInfoBySellerId End!");


        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }

    @GetMapping("/order/order/{orderId}")
    @Timed(value = "order.findById", longTask = true)
    public ResponseEntity<Map<String, Object>> findOrderInfoByOrderId(@PathVariable String orderId) throws Exception {
        Map<String, Object> rMap = new HashMap<>();

        OrderDto orderDto = orderService.findOneOrderByOrderId(orderId);

        rMap.put("response", orderDto);

        return ResponseEntity.ok().body(rMap);
    }
}

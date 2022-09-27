package com.submarket.orderservice.service;

import com.submarket.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {

    int insertOrder(OrderDto orderDto) throws Exception;

    List<OrderDto> findAllOrderByUserId(String userId) throws Exception;

    List<OrderDto> findAllOrderBySellerId(String sellerId) throws Exception;

    OrderDto findOneOrderByOrderId(String orderId) throws Exception;

    int totalPriceByItemSeq(OrderDto orderDto, int itemPrice) throws Exception;
}

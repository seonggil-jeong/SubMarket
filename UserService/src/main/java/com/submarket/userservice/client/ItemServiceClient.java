package com.submarket.userservice.client;

import com.submarket.userservice.dto.ItemDto;
import com.submarket.userservice.vo.ItemInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "ITEM-SERVICE")
public interface ItemServiceClient {
    @GetMapping("/items/{itemSeq}") // 상품이 확인
    ResponseEntity<ItemDto> isItem(@PathVariable int itemSeq);


    @GetMapping("/circuit/items/{itemSeq}") // 상품이 확인
    ResponseEntity<ItemInfoResponse> findOneItem(@PathVariable int itemSeq);
}

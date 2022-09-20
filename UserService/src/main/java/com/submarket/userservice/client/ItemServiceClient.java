package com.submarket.userservice.client;

import com.submarket.userservice.dto.ItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "item-service")
public interface ItemServiceClient {
    @GetMapping("/items/{itemSeq}") // 상품이 확인
    ItemDto isItem(@PathVariable int itemSeq);
}

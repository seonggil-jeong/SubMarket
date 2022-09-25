package com.submarket.itemservice.client;

import com.submarket.itemservice.vo.ItemLikedRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/items/liked")
    ResponseEntity<Integer> isLikedByUserId(@RequestHeader final HttpHeaders headers,
                        @RequestBody @Validated final ItemLikedRequest request);
}

package com.submarket.itemservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    // Gateway 를 통과하지 않고 내부 통신으로 이동하기 때문에 "/user-service" 가 필요하지 않음
    @GetMapping("/users/{userId}/items/{itemSeq}/liked")
    ResponseEntity<Integer> isLikedByUserId(@PathVariable int itemSeq, @PathVariable String userId);
}

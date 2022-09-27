package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemServiceImpl;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item ReadCount API", description = "상품 조회 수 관련 API")
public class ReadCountController {
    private final ItemServiceImpl itemService;


    @Operation(summary = "사용자 나이별 상품 조회", description = "나이별 선호하는 상품 순으로 정렬 후 전달", tags = {"user", "item"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/item/read/{age}")
    @Timed(value = "item.item.sort.readCount", longTask = true)
    public ResponseEntity<Map<String, Object>> findItemInfoByReadCount(@PathVariable int age) throws Exception {
        log.info(this.getClass().getName() + ".");
        Map<String, Object> rMap = new HashMap<>();

        List<ItemDto> itemDtoList = itemService.findAllItem();
        Collections.sort(itemDtoList, new Comparator<ItemDto>() {
            @Override
            public int compare(ItemDto o1, ItemDto o2) {
                if (age > 0 && age <= 29) {
                    int age1 = o1.getReadCount20();
                    int age2 = o2.getReadCount20();
                    return Integer.compare(age2, age1);

                } else if (age >= 30 && age <= 39) {
                    int age1 = o1.getReadCount30();
                    int age2 = o2.getReadCount30();
                    return Integer.compare(age2, age1);
                } else if (age >= 40 && age <= 49) {
                    int age1 = o1.getReadCount40();
                    int age2 = o2.getReadCount40();
                    return Integer.compare(age2, age1);
                } else {
                    int age1 = o1.getReadCountOther();
                    int age2 = o2.getReadCountOther();
                    return Integer.compare(age2, age1);
                }
            }
        });

        rMap.put("response", itemDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(rMap);
    }

    @Operation(summary = "사용자 나이별 상품 조회 수 증가", description = "사용자 나이를 조회하여 조회 수 증가", tags = {"user", "item"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "조회 수 증가 성공")
    })
    @GetMapping("/item/{itemSeq}/countUp/{userAge}")
    @Timed(value = "item.count.up.age", longTask = true)
    public ResponseEntity<String> itemCountUp(@PathVariable int itemSeq, @PathVariable int userAge) throws Exception {
        log.info(this.getClass().getName() + ".itemCountUp Start");
        itemService.upReadCount(itemSeq, userAge);

        return ResponseEntity.status(HttpStatus.CREATED).body("UpCount");
    }

}

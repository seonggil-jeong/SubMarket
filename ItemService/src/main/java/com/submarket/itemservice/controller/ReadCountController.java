package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ReadCountController {
    private final ItemService itemService;


    @GetMapping("/item/read/{age}")
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

    @PostMapping("/item/{itemSeq}/countUp/{userAge}")
    public ResponseEntity<String> itemCountUp(@PathVariable int itemSeq, @PathVariable int userAge) throws Exception {
        log.info(this.getClass().getName() + ".itemCountUp Start");
        itemService.upCount(itemSeq, userAge);

        return ResponseEntity.status(HttpStatus.CREATED).body("UpCount");
    }

}

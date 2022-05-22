package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    // TODO: 2022/05/16 로직 추가
    @GetMapping("/items")
    public ResponseEntity<List<ItemDto>> findAllItem() throws Exception {
        log.info(this.getClass().getName() + ".findAllItem Start");

        List<ItemDto> itemDtoList = itemService.findAllItem();

        log.info(this.getClass().getName() + ".findAllItem End");
        return ResponseEntity.ok().body(itemDtoList);
    }

    @GetMapping("/items/{itemSeq}")
    public ResponseEntity<Object> findOneItem(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneItem Start! (itemSeq : " + itemSeq + ")");

        ItemDto pDto = new ItemDto();
        pDto.setItemSeq(itemSeq);

        // 상품 정보 가져오기
        ItemDto itemDto = itemService.findItemInfo(pDto);

        if (itemDto.equals(null)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품 정보를 찾을 수 없습니다");
        }


        log.info(this.getClass().getName() + ".findOneItem End!");
        return ResponseEntity.ok().body(itemDto);
    }
}

package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemService;
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
public class ItemController {
    private final ItemService itemService;

    // TODO: 2022/05/16 로직 추가
    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> findAllItem() throws Exception {
        log.info(this.getClass().getName() + ".findAllItem Start");
        Map<String, Object> rMap = new HashMap<>();

        List<ItemDto> itemDtoList = itemService.findAllItem();

        rMap.put("response", itemDtoList);

        log.info(this.getClass().getName() + ".findAllItem End");
        return ResponseEntity.ok().body(rMap);
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

    @DeleteMapping("/items/{itemSeq}")
    public ResponseEntity<String> offItem(@PathVariable int itemSeq) throws Exception {
        // TODO: 2022/05/16 비활성화, 사업자 인증
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.offItem(itemDto);
        return ResponseEntity.ok().body("비활성화 완료");
    }

    @PatchMapping("/items/{itemSeq}")
    public ResponseEntity<String> onItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.onItem(itemDto);
        return ResponseEntity.ok().body("활성화 완료");
    }

    @PutMapping("/items")
    public ResponseEntity<String> modifyItem(@RequestBody ItemDto itemDto) throws Exception {
        // TODO: 2022-05-16 상품 이미지 로직 추가
        log.info(this.getClass().getName());

        itemService.modifyItem(itemDto);

        return ResponseEntity.ok().body("상품 수정 완료");
    }
}

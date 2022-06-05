package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemService;
import com.submarket.itemservice.util.CmmUtil;
import com.submarket.itemservice.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final TokenUtil tokenUtil;

    // TODO: 2022/05/16 로직 추가

    @PostMapping(value = "/items", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> saveItem(@RequestHeader HttpHeaders headers, ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start!");

        String sellerId = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemDto.setSellerId(sellerId);

        if (itemDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 정보 오류");
        }
        int res = itemService.saveItem(itemDto);

        if (res == 0) {
            ResponseEntity.status(500).body("ServerError");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록 완료");
    }

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

    @PostMapping("/items/{itemSeq}/off")
    public ResponseEntity<String> offItem(@PathVariable int itemSeq) throws Exception {
        // TODO: 2022/05/16 비활성화, 사업자 인증
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.offItem(itemDto);
        return ResponseEntity.ok().body("비활성화 완료");
    }

    @PostMapping("/items/{itemSeq}/on")
    public ResponseEntity<String> onItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.onItem(itemDto);
        return ResponseEntity.ok().body("활성화 완료");
    }

    @PostMapping("/items/modify")
    public ResponseEntity<String> modifyItem(@RequestBody ItemDto itemDto) throws Exception {
        // TODO: 2022-05-16 상품 이미지 로직 추가
        log.info(this.getClass().getName());

        itemService.modifyItem(itemDto);

        return ResponseEntity.ok().body("상품 수정 완료");
    }
}

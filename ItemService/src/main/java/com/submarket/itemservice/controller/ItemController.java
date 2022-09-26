package com.submarket.itemservice.controller;

import com.submarket.itemservice.client.UserServiceClient;
import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.ItemService;
import com.submarket.itemservice.service.impl.ItemServiceImpl;
import com.submarket.itemservice.util.CmmUtil;
import com.submarket.itemservice.util.TokenUtil;
import com.submarket.itemservice.vo.ItemInfoResponse;
import com.submarket.itemservice.vo.ItemLikedRequest;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item API", description = "상품 정보 API")
public class ItemController {
    private final ItemService itemService;
    private final TokenUtil tokenUtil;

    private final UserServiceClient userServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;


    @Operation(summary = "상품 정보 등록", description = "상품 정보를 등록", tags = {"seller", "item"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 정보 등록 성공"),
            @ApiResponse(responseCode = "400", description = "상품 이미지 파일 변환 실패"),
            @ApiResponse(responseCode = "400", description = "상품 이미지 저장 실패"),
            @ApiResponse(responseCode = "500", description = "서버 Error")
    })
    @PostMapping(value = "/items", consumes = MediaType.ALL_VALUE)
    @Timed(value = "item.save", longTask = true)
    public ResponseEntity<String> saveItem(@RequestHeader HttpHeaders headers, ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start!");

        String sellerId = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemDto.setSellerId(sellerId);

        if (itemDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 정보 오류");
        }
        itemService.saveItem(itemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록 완료");
    }


    @Operation(summary = "모든 상품 조회", description = "모든 상품 정보 조회", tags = {"item"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 정보 조회 성공")
    })
    @GetMapping("/items")
    @Timed(value = "item.findAll", longTask = true)
    public ResponseEntity<Map<String, Object>> findAllItem() throws Exception {
        log.info(this.getClass().getName() + ".findAllItem Start");
        Map<String, Object> rMap = new HashMap<>();

        List<ItemDto> itemDtoList = itemService.findAllItem();

        rMap.put("response", itemDtoList);

        log.info(this.getClass().getName() + ".findAllItem End");
        return ResponseEntity.ok().body(rMap);
    }


    @Operation(summary = "상품 정보 상세 조회", description = "상품 번호를 사용하여 상품 정보 상세 조회", tags = {"item"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품 정보를 찾을 수 없음")
    })
    @GetMapping("/items/{itemSeq}")
    @Timed(value = "item.findOne", longTask = true)
    public ResponseEntity<ItemDto> findOneItem(@RequestHeader HttpHeaders headers,
                                               @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneItem Start! (itemSeq : " + itemSeq + ")");

        ItemDto pDto = new ItemDto();
        pDto.setItemSeq(itemSeq);

        // 상품 정보 가져오기
        ItemDto itemDto = itemService.findItemInfo(pDto);

        log.info("circuit Start");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("userCircuit");

        ResponseEntity<Integer> circuitResult =
                circuitBreaker.run(() -> userServiceClient.isLikedByUserId
                                (itemSeq, tokenUtil.getUserIdByToken(headers)),
                        throwable -> ResponseEntity.ok().body(0));

        log.info("circuit End");

        itemDto.setIsUserLiked(circuitResult.getBody());

        log.info(this.getClass().getName() + ".findOneItem End!");
        return ResponseEntity.ok().body(itemDto);
    }


    @Operation(summary = "상품 비활성화", description = "상품 비활성화를 통해 노출 금지", tags = {"item", "seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 정보 비활성화 성공")
    })
    @PostMapping("/items/{itemSeq}/off")
    @Timed(value = "item.off", longTask = true)
    public ResponseEntity<String> offItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.offItem(itemDto);
        return ResponseEntity.ok().body("비활성화 완료");
    }


    @Operation(summary = "상품 활성화", description = "상품 활성화", tags = {"item", "seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 정보 활성화 성공")
    })
    @PostMapping("/items/{itemSeq}/on")
    @Timed(value = "item.on", longTask = true)
    public ResponseEntity<String> onItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.onItem(itemDto);
        return ResponseEntity.ok().body("활성화 완료");
    }


    @Operation(summary = "상품 정보 수정", description = "상품 정보 변경", tags = {"item", "seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 정보 변경 성공"),
            @ApiResponse(responseCode = "404", description = "상품 번호와 일치하는 상품 정보가 없음")
    })
    @PostMapping(value = "/items/modify/{itemSeq}", consumes = MediaType.ALL_VALUE)
    @Timed(value = "item.modify", longTask = true)
    public ResponseEntity<String> modifyItem(ItemDto itemDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".modifyItem Start!");
        itemDto.setItemSeq(itemSeq);

        itemService.modifyItem(itemDto);

        return ResponseEntity.ok().body("상품 수정 완료");
    }


    @Operation(summary = "상품 정보 조회 수 증가", description = "상품 조회 및 리뷰 작성 시 조회 수 증가", tags = {"item", "kafka"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 조회수 증가 성공")
    })
    @PostMapping("/items/{itemSeq}/up")
    @Timed(value = "item.count.up", longTask = true)
    public void upCount(@PathVariable int itemSeq, @RequestBody Map<String, Object> request) throws Exception {
        int userAge = Integer.parseInt(String.valueOf(request.get("userAge")));
        log.info(this.getClass().getName() + ".upCount Start!");
        itemService.upReadCount(itemSeq, userAge);

        log.info(this.getClass().getName() + ".upCount End!");
    }


    @GetMapping("/circuit/items/{itemSeq}")
    @Timed(value = "item.findOne", longTask = true)
    public ResponseEntity<ItemInfoResponse> circuitFindOneItem(@RequestHeader HttpHeaders headers,
                                                            @PathVariable int itemSeq) throws Exception {

        ItemDto pDto = new ItemDto();
        pDto.setItemSeq(itemSeq);

        // 상품 정보 가져오기
        ItemDto result = itemService.findItemInfo(pDto);
        return ResponseEntity.ok().body(ItemInfoResponse.builder()
                .itemSeq(result.getItemSeq())
                .itemPrice(result.getItemPrice())
                .itemStatus(result.getItemStatus())
                .itemContents(result.getItemContents())
                .itemTitle(result.getItemTitle())
                .sellerId(result.getSellerId())
                .itemCount(result.getItemCount())
                .categorySeq(result.getCategorySeq())
                .readCount20(result.getReadCount20())
                .readCount30(result.getReadCount30())
                .readCount40(result.getReadCount40())
                .readCountOther(result.getReadCountOther())
                .mainImagePath(result.getMainImagePath())
                .subImagePath(result.getSubImagePath()).build()
        );
    }
}

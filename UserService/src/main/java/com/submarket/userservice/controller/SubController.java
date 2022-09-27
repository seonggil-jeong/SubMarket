package com.submarket.userservice.controller;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.mapper.SubMapper;
import com.submarket.userservice.service.SubService;
import com.submarket.userservice.service.impl.SubServiceImpl;
import com.submarket.userservice.util.TokenUtil;
import com.submarket.userservice.vo.SubRequest;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "구독 API", description = "구독 관련 API")
public class SubController {
    private final SubService subServiceImpl;
    private final TokenUtil tokenUtil;

    @Operation(summary = "구독중인 상품 전체 조회", description = "사용자가 구독중인 모든 상품 점보를 조회", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 구독 정보 조회 성공")
    })
    @GetMapping("/subs")
    @Timed(value = "user.sub.findAllSub", longTask = true)
    public ResponseEntity<Map<String, Object>> findAllSub(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".findSub Start!");

        Map<String, Object> rMap = new HashMap<>();

        String userId = tokenUtil.getUserIdByToken(headers);


        SubDto subDto = new SubDto();
        subDto.setUserId(userId);
        List<SubEntity> subEntityList = subServiceImpl.findAllSub(subDto);

        List<SubDto> subDtoList = new ArrayList<>();

        subEntityList.forEach(subEntity -> {
            subDtoList.add(SubMapper.INSTANCE.subEntityToSubDto(subEntity));
        });

        rMap.put("response", subDtoList);

        return ResponseEntity.ok().body(rMap);


    }


    @Operation(summary = "구독중인 상품 상세 조회", description = "사용자가 가지고 있는 구독 상품 정보 상세 조회", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 구독 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "Seq 값과 일치하는 구독 정보 없음")
    })
    @GetMapping("/subs/{subSeq}")
    @Timed(value = "user.sub.findOneSub", longTask = true)
    public ResponseEntity<SubDto> findOneSub(@PathVariable int subSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneSub Start!");
        SubDto pDto = new SubDto();

        pDto.setSubSeq(subSeq);

        SubDto subDto = subServiceImpl.findOneSub(pDto);

        if (subDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        log.info(this.getClass().getName() + ".findOneSub Start!");

        return ResponseEntity.ok().body(subDto);
    }

    @Operation(summary = "상품 구독 생성",
            description = "사용자가 상품 주문 시 구독 생성 및 Kafka 를 통해 Item 수량 감소, Order 정보 생성", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자 구독 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 구독 생성")
    })
    @PostMapping("/subs")
    @Timed(value = "user.sub.createSub", longTask = true)
    public ResponseEntity<String> createNewSub(@RequestHeader HttpHeaders headers,
                                               @RequestBody SubDto subDto) throws Exception {
        log.info(this.getClass().getName() + ".createNewSub Start!");

        String userId = tokenUtil.getUserIdByToken(headers);

        subDto.setUserId(userId);


        int res = subServiceImpl.createNewSub(subDto);

        if (res == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 구독");
        }

        if (res != 1) {
            return ResponseEntity.status(500).body("오류");
        }

        log.info(this.getClass().getName() + ".createNewSub End! ");

        return ResponseEntity.status(HttpStatus.CREATED).body("구독 성공");
    }

    @Operation(summary = "상품 구독 취소",
            description = "상품 구독 취소 및 Item 수량 복구", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 구독 취소 성공")
    })
    @PostMapping("/subs/delete")
    @Timed(value = "user.sub.deleteSub", longTask = true)
    public ResponseEntity<String> cancelSub(@RequestBody SubRequest subRequest) throws Exception {
        log.info(this.getClass().getName() + "cancel Sub Start!");

        SubDto subDto = new SubDto();

        subDto.setSubSeq(subRequest.getSubSeq());

        int res = subServiceImpl.cancelSub(subDto);


        log.info(this.getClass().getName() + "cancel Sub End!");

        if (res != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("구독 취소 실패");
        }
        return ResponseEntity.status(HttpStatus.OK).body("구독 취소 성공");
    }


    @Operation(summary = "상품 구독 갱신",
            description = "상품 구독 갱신 및 주문 생성 (월 단위 자동 호출)", tags = {"user"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 구독 갱신 성공")
    })
    @PostMapping("/subs/update")
    @Timed(value = "user.sub.updateUsb", longTask = true)
    public ResponseEntity<String> updateSub(@RequestBody SubRequest subRequest) throws Exception {
        log.info(this.getClass().getName() + ".updateSub Start!");
        SubDto subDto = new SubDto();
        subDto.setSubSeq(subRequest.getSubSeq());

        int res = subServiceImpl.updateSub(subDto);

        if (res != 1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("갱신 실패");
        }

        log.info(this.getClass().getName() + "updateSub End!");
        return ResponseEntity.ok("갱신 완료");


    }


    @Operation(summary = "판매자 상품 Count 조회",
            description = "판매중인 상품 총 구독 수 조회", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 상품 조회 성공")
    })
    @GetMapping("/sellers/subs")
    @Timed(value = "seller.sub.findSubCount", longTask = true)
    public ResponseEntity<Integer> findSubCount(@RequestBody Map<String, Object> request) throws Exception {
        // Seller 가 보유하고 있는 상품의 SeqList 를 넘겨주면 총 구독 수를 표시
        log.info(this.getClass().getName() + "findSubCount");
        List<Integer> itemSeqList = new LinkedList<>();
        itemSeqList = (List<Integer>) request.get("itemSeqList");

        int count = subServiceImpl.findSubCount(itemSeqList);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @Operation(summary = "판매자 단일 상품 구독 수 조회",
            description = "판매중인 상품 별 구독 정보 조회", tags = {"seller"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "판매자 상품 조회 성공")
    })
    @GetMapping("/sellers/subs/{itemSeq}")
    @Timed(value = "seller.sub.findOneSub", longTask = true)
    public ResponseEntity<Integer> findOneSubCount(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + "findOneSubCount Start!");

        int count = subServiceImpl.findOneSubCount(itemSeq);

        log.info(this.getClass().getName() + "findOneSubCount End!");

        return ResponseEntity.ok().body(count);
    }
}

package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.service.impl.ItemReviewService;
import com.submarket.itemservice.util.CmmUtil;
import com.submarket.itemservice.util.DateUtil;
import com.submarket.itemservice.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemReviewController {
    private final ItemReviewService itemReviewService;
    private final TokenUtil tokenUtil;

    @PostMapping("/item/{itemSeq}/review")
    public ResponseEntity<String> saveReview(@RequestHeader HttpHeaders headers,
                                             @RequestBody ItemReviewDto itemReviewDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".saveReview Start!");
        // TODO: 2022-05-16 사용자가 이미 작성한 리뷰가 있는지 확인 with UserSeq

        String userId = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemReviewDto.setUserId(userId);
        itemReviewDto.setReviewDate(DateUtil.getDateTime("yyyyMMdd"));

        if (itemReviewDto.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 정보를 입력해주세요");
        }

        int res = itemReviewService.saveReview(itemReviewDto, itemSeq);


        log.info(this.getClass().getName() + ".saveReview End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 완료");
    }

    @PostMapping("/item/review/{reviewSeq}/modify")
    public ResponseEntity<String> modifyItemReview(@RequestBody ItemReviewDto itemReviewDto, @PathVariable int reviewSeq)
        throws Exception {
        log.info(this.getClass().getName() + ".modifyItemReview Start!");

        itemReviewDto.setReviewSeq(reviewSeq);
        int res = itemReviewService.modifyReview(itemReviewDto);


        log.info(this.getClass().getName() + ".modifyItemReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 변경 완료");
    }

    @PostMapping("/item/review/{reviewSeq}/delete")
    public ResponseEntity<String> deleteItemReview(@PathVariable int reviewSeq) throws Exception {
        log.info(this.getClass().getName() + ".deleteReview Start!");

        ItemReviewDto itemReviewDto = new ItemReviewDto();
        itemReviewDto.setReviewSeq(reviewSeq);

        itemReviewService.deleteReview(itemReviewDto);

        log.info(this.getClass().getName() + ".deleteReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 완료");
    }

    @GetMapping("/item/{itemSeq}/review")
    public ResponseEntity<Map<String, Object>> findItemReviewInItem(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + "findItemReviewInItem Start!");

        Map<String, Object> rMap = new HashMap<>();

        List<ItemReviewDto> itemReviewDtoList = itemReviewService.findAllReviewInItem(itemSeq);


        rMap.put("response", itemReviewDtoList);

        log.info(this.getClass().getName() + "findItemReviewInItem End!");

        return ResponseEntity.ok().body(rMap);
    }

    @GetMapping("/item/review")
    public ResponseEntity<Map<String, Object>> findReviewByUserId(@RequestHeader HttpHeaders headers) throws Exception {
        String userId = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));

        Map<String, Object> rMap = new HashMap<>();

        List<ItemReviewDto> itemReviewDtoList = itemReviewService.findAllReviewByUserId(userId);

        if (itemReviewDtoList == null) {
            itemReviewDtoList = new LinkedList<ItemReviewDto>();
        }

        rMap.put("response", itemReviewDtoList);


        return ResponseEntity.ok().body(rMap);
    }

}
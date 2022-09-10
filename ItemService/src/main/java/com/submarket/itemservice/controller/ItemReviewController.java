package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.exception.ItemReviewException;
import com.submarket.itemservice.exception.result.ItemReviewExceptionResult;
import com.submarket.itemservice.service.ItemReviewCheckService;
import com.submarket.itemservice.service.ItemReviewService;
import com.submarket.itemservice.service.impl.ItemReviewCheckServiceImpl;
import com.submarket.itemservice.service.impl.ItemReviewServiceImpl;
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
    private final ItemReviewCheckService itemReviewCheckServiceImpl;

    @PostMapping("/item/{itemSeq}/review")
    public ResponseEntity<String> saveReview(@RequestHeader HttpHeaders headers,
                                             @RequestBody ItemReviewDto itemReviewDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".saveReview Start!");
        final String userIdByToken = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemReviewDto.setReviewDate(DateUtil.getDateTime("yyyyMMdd"));

        if (itemReviewDto == null) {
            throw new ItemReviewException(ItemReviewExceptionResult.ITEM_REVIEW_IS_NULL);
        }

        if (itemReviewCheckServiceImpl.canCreateReview(ItemReviewDto.builder() // 리뷰 생성 로직 Check
                .userId(userIdByToken)
                .reviewDate(DateUtil.getDateTime("yyyyMMdd")).build(), itemSeq)) {

            itemReviewService.saveReview(itemReviewDto, itemSeq);

        } else {
            throw new ItemReviewException(ItemReviewExceptionResult.ITEM_REVIEW_ALREADY_CREATED);
        }

        log.debug(this.getClass().getName() + ".saveReview End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 완료");
    }

    @PostMapping("/item/review/{reviewSeq}/modify")
    public ResponseEntity<String> modifyItemReview(@RequestBody ItemReviewDto itemReviewDto, @PathVariable int reviewSeq)
            throws Exception {
        log.debug(this.getClass().getName() + ".modifyItemReview Start!");


        itemReviewService.modifyReview(ItemReviewDto.builder().reviewSeq(reviewSeq).build());


        log.debug(this.getClass().getName() + ".modifyItemReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 변경 완료");
    }

    @PostMapping("/item/review/{reviewSeq}/delete")
    public ResponseEntity<String> deleteItemReview(@PathVariable int reviewSeq) throws Exception {
        log.debug(this.getClass().getName() + ".deleteReview Start!");

        itemReviewService.deleteReview(ItemReviewDto.builder().reviewSeq(reviewSeq).build());

        log.debug(this.getClass().getName() + ".deleteReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 완료");
    }

    @GetMapping("/item/{itemSeq}/review")
    public ResponseEntity<Map<String, Object>> findItemReviewInItem(@PathVariable int itemSeq) throws Exception {
        log.debug(this.getClass().getName() + "findItemReviewInItem Start!");
        log.debug(this.getClass().getName() + "findItemReviewInItem End!");

        return ResponseEntity.ok().body(new HashMap<String, Object>(){
            {
                put("response", itemReviewService.findAllReviewInItem(itemSeq));
            }
        });
    }

    @GetMapping("/item/review")
    public ResponseEntity<Map<String, Object>> findReviewByUserId(@RequestHeader HttpHeaders headers) throws Exception {
        final String userIdByToken = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));

        return ResponseEntity.ok().body(new HashMap<String, Object>(){
            {
                put("response", itemReviewService.findAllReviewByUserId(userIdByToken));
            }
        });
    }

}
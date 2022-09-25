package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.exception.ItemReviewException;
import com.submarket.itemservice.exception.result.ItemReviewExceptionResult;
import com.submarket.itemservice.service.ItemReviewCheckService;
import com.submarket.itemservice.service.ItemReviewService;
import com.submarket.itemservice.util.CmmUtil;
import com.submarket.itemservice.util.DateUtil;
import com.submarket.itemservice.util.TokenUtil;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item Review API", description = "상품 정보 리뷰 API")
public class ItemReviewController {
    private final ItemReviewService itemReviewService;
    private final TokenUtil tokenUtil;
    private final ItemReviewCheckService itemReviewCheckService;


    @Operation(summary = "상품 리뷰 등록", description = "상품 리뷰 등록 성공", tags = {"user", "review"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 리뷰 생성 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 리뷰 정보"),
            @ApiResponse(responseCode = "400", description = "리뷰 정보 유효성 검사 실패")
    })
    @PostMapping("/item/{itemSeq}/review")
    @Timed(value = "item.review.save", longTask = true)
    public ResponseEntity<String> saveReview(@RequestHeader HttpHeaders headers,
                                             @RequestBody ItemReviewDto itemReviewDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".saveReview Start!");
        final String userIdByToken = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemReviewDto.setReviewDate(DateUtil.getDateTime("yyyyMMdd"));

        if (itemReviewDto == null) {
            throw new ItemReviewException(ItemReviewExceptionResult.ITEM_REVIEW_IS_NULL);
        }

        if (itemReviewCheckService.canCreateReview(ItemReviewDto.builder() // 리뷰 생성 로직 Check
                .userId(userIdByToken)
                .reviewDate(DateUtil.getDateTime("yyyyMMdd")).build(), itemSeq)) {

            itemReviewService.saveReview(itemReviewDto, itemSeq);

        } else {
            throw new ItemReviewException(ItemReviewExceptionResult.ITEM_REVIEW_ALREADY_CREATED);
        }

        log.debug(this.getClass().getName() + ".saveReview End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 완료");
    }

    @Operation(summary = "상품 리뷰 정보 변경", description = "상품 리뷰 정보 변경", tags = {"user", "review"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 정보 변경 성공"),
    })
    @PostMapping("/item/review/{reviewSeq}/modify")
    @Timed(value = "item.review.modify", longTask = true)
    public ResponseEntity<String> modifyItemReview(@RequestBody ItemReviewDto itemReviewDto, @PathVariable int reviewSeq)
            throws Exception {
        log.debug(this.getClass().getName() + ".modifyItemReview Start!");


        itemReviewService.modifyReview(ItemReviewDto.builder().reviewSeq(reviewSeq).build());


        log.debug(this.getClass().getName() + ".modifyItemReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 변경 완료");
    }


    @Operation(summary = "상품 리뷰 정보 삭제", description = "상품 리뷰 정보 삭제", tags = {"user", "review"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 정보 삭제 성공"),
    })
    @PostMapping("/item/review/{reviewSeq}/delete")
    @Timed(value = "item.review.delete", longTask = true)
    public ResponseEntity<String> deleteItemReview(@PathVariable int reviewSeq) throws Exception {
        log.debug(this.getClass().getName() + ".deleteReview Start!");

        itemReviewService.deleteReview(ItemReviewDto.builder().reviewSeq(reviewSeq).build());

        log.debug(this.getClass().getName() + ".deleteReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 완료");
    }


    @Operation(summary = "상품 리뷰 정보 조회", description = "상품에 달린 리뷰 정보 전체 조회", tags = {"item", "review"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 정보 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 상품 정보가 없음")
    })
    @GetMapping("/item/{itemSeq}/review")
    @Timed(value = "item.review.findReviewInfo", longTask = true)
    public ResponseEntity<Map<String, Object>> findItemReviewInItem(@PathVariable int itemSeq) throws Exception {
        log.debug(this.getClass().getName() + "findItemReviewInItem Start!");
        log.debug(this.getClass().getName() + "findItemReviewInItem End!");

        return ResponseEntity.ok().body(new HashMap<String, Object>(){
            {
                put("response", itemReviewService.findAllReviewInItem(itemSeq));
            }
        });
    }

    @Operation(summary = "상품 리뷰 정보 조회", description = "특정 사용자가 작성한 리뷰 목록 조회", tags = {"user", "review"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 정보 조회 성공"),
    })
    @GetMapping("/item/review")
    @Timed(value = "user.item.review.find", longTask = true)
    public ResponseEntity<Map<String, Object>> findReviewByUserId(@RequestHeader HttpHeaders headers) throws Exception {
        final String userIdByToken = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));

        return ResponseEntity.ok().body(new HashMap<String, Object>(){
            {
                put("response", itemReviewService.findAllReviewByUserId(userIdByToken));
            }
        });
    }

}
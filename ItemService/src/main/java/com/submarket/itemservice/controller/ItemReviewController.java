package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.service.impl.ItemReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemReviewController {
    private final ItemReviewService itemReviewService;

    @PostMapping("/item/{itemSeq}/review")
    public ResponseEntity<String> saveReview(@RequestBody ItemReviewDto itemReviewDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".saveReview Start!");
        // TODO: 2022-05-16 사용자가 이미 작성한 리뷰가 있는지 확인 with UserSeq

        if (itemReviewDto.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 정보를 입력해주세요");
        }

        int res = itemReviewService.saveReview(itemReviewDto, itemSeq);


        log.info(this.getClass().getName() + ".saveReview End!");

        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 작성 완료");
    }

    @PatchMapping("/item/review/{reviewSeq}")
    public ResponseEntity<String> modifyItemReview(@RequestBody ItemReviewDto itemReviewDto, @PathVariable int reviewSeq)
        throws Exception {
        log.info(this.getClass().getName() + ".modifyItemReview Start!");

        itemReviewDto.setReviewSeq(reviewSeq);
        int res = itemReviewService.modifyReview(itemReviewDto);


        log.info(this.getClass().getName() + ".modifyItemReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 변경 완료");
    }

    @DeleteMapping("/item/review/{reviewSeq}")
    public ResponseEntity<String> deleteItemReview(@PathVariable int reviewSeq) throws Exception {
        log.info(this.getClass().getName() + ".deleteReview Start!");

        ItemReviewDto itemReviewDto = new ItemReviewDto();
        itemReviewDto.setReviewSeq(reviewSeq);

        itemReviewService.deleteReview(itemReviewDto);

        log.info(this.getClass().getName() + ".deleteReview End!");

        return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 완료");
    }

    @GetMapping("/item/{itemSeq}/review")
    public ResponseEntity<List<ItemReviewDto>> findItemReviewInItem(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + "findItemReviewInItem Start!");

        List<ItemReviewDto> itemReviewDtoList = itemReviewService.findAllReviewInItem(itemSeq);

        log.info(this.getClass().getName() + "findItemReviewInItem End!");

        return ResponseEntity.ok().body(itemReviewDtoList);
    }

}
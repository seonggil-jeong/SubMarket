package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.ItemReviewDto;

import java.util.List;

public interface ItemReviewService {
    void saveReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception;

    void modifyReview(ItemReviewDto itemReviewDto) throws Exception;

    void deleteReview(ItemReviewDto itemReviewDto) throws Exception;

    List<ItemReviewDto> findAllReviewInItem(int itemSeq) throws Exception;

    List<ItemReviewDto> findAllReviewByUserId(String userId) throws Exception;
}
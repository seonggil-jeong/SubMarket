package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.ItemReviewDto;

public interface ItemReviewCheckService {
    boolean canCreateReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception;
}

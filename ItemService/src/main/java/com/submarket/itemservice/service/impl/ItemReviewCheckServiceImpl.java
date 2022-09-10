package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.exception.ItemException;
import com.submarket.itemservice.exception.ItemReviewException;
import com.submarket.itemservice.exception.result.ItemExceptionResult;
import com.submarket.itemservice.exception.result.ItemReviewExceptionResult;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.ItemReviewRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.service.ItemReviewCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j

public class ItemReviewCheckServiceImpl implements ItemReviewCheckService {
    private final ItemReviewRepository itemReviewRepository;
    private final ItemRepository itemRepository;

    @Override
    public boolean canCreateReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception {
        log.debug(this.getClass().getName() + ".canCreateReview Start!");

        ItemEntity item = itemRepository.findById(itemSeq)
                .orElseThrow(() -> new ItemException(ItemExceptionResult.ITEM_NOT_FOUND));


        if (itemReviewRepository.findByUserIdAndItem(itemReviewDto.getUserId(), item).isPresent()) {

            throw new ItemReviewException(ItemReviewExceptionResult.ITEM_REVIEW_ALREADY_CREATED);

        } else {
            return true;
        }
    }
}

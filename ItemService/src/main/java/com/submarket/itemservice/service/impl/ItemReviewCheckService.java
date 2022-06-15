package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.ItemReviewRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import com.submarket.itemservice.service.IItemReviewCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ItemReviewCheckService implements IItemReviewCheckService {
    private final ItemReviewRepository itemReviewRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public boolean canCreateReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".canCreateReview Start!");
        String userId = itemReviewDto.getUserId();

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);
        ItemEntity item = itemEntityOptional.get();

        Optional<ItemReviewEntity> itemReviewEntity = itemReviewRepository.findByUserIdAndItem(userId, item);

        log.info(this.getClass().getName() + ".canCreateReview End!");

        if (itemReviewEntity.isPresent()) {
            log.info("이미 작성한 리뷰가 있음");
            return false;
        }
        return true;
    }
}

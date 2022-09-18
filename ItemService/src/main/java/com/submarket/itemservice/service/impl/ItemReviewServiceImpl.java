package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.exception.ItemException;
import com.submarket.itemservice.exception.result.ItemExceptionResult;
import com.submarket.itemservice.jpa.ItemReviewRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import com.submarket.itemservice.mapper.ItemMapper;
import com.submarket.itemservice.mapper.ItemReviewMapper;
import com.submarket.itemservice.service.ItemReviewService;
import com.submarket.itemservice.service.ItemService;
import com.submarket.itemservice.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemReviewServiceImpl implements ItemReviewService {
    private final ItemReviewRepository itemReviewRepository;
    private final ItemService itemService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception {
        log.debug(this.getClass().getName() + ".saveReview Start!");

        int userAge = Integer.parseInt(itemReviewDto.getUserAge());

        ItemDto items = itemService.findItemInfo(ItemDto.builder().itemSeq(itemSeq).build());
        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(items);
        itemReviewDto.setItem(itemEntity);


        itemService.upReadCount(itemSeq, userAge, itemReviewDto.getReviewStar()); // readCount += Review Star

        itemReviewRepository.save(ItemReviewMapper.INSTANCE.itemReviewDtoToItemEntity(itemReviewDto));

        log.info(this.getClass().getName() + ".saveReview End!");

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void modifyReview(ItemReviewDto itemReviewDto) throws Exception {
        log.info(this.getClass().getName() + ".modifyReview Start!");

        itemReviewRepository.modifyItemReview(itemReviewDto.getReviewContents(),
                itemReviewDto.getReviewDate(),
                itemReviewDto.getReviewStar(),
                itemReviewDto.getReviewSeq());

        log.info(this.getClass().getName() + ".modifyReview End!");

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteReview(ItemReviewDto itemReviewDto) throws Exception {
        log.info(this.getClass().getName() + ".deleteReview Start!");

        itemReviewRepository.deleteById(itemReviewDto.getReviewSeq());

        log.info(this.getClass().getName() + ".deleteReview End!");
    }

    @Override
    public List<ItemReviewDto> findAllReviewInItem(final int itemSeq) throws Exception {

        ItemDto rDto = itemService.findItemInfo(ItemDto.builder().itemSeq(itemSeq).build());

        if (rDto == null) {
            throw new ItemException(ItemExceptionResult.ITEM_NOT_FOUND);
        }

        List<ItemReviewDto> reviewDtoList = new LinkedList<>();

        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(rDto);
        List<ItemReviewEntity> result = itemReviewRepository.findByItem(itemEntity);

        result.forEach(itemReviewEntity -> {
            reviewDtoList.add(ItemReviewMapper.INSTANCE.itemReviewEntityToItemDto(itemReviewEntity));
        });


        return reviewDtoList;
    }

    @Override
    public List<ItemReviewDto> findAllReviewByUserId(String userId) throws Exception {
        log.info(this.getClass().getName() + ".findAllReviewByUserId Start!");

        List<ItemReviewDto> itemReviewDtoList = new LinkedList<>();


        List<ItemReviewEntity> result = itemReviewRepository.findAllByUserId(CmmUtil.nvl(userId));

        if (result.isEmpty()) {
            return new LinkedList<ItemReviewDto>();
        }

        result.forEach(e -> {
            itemReviewDtoList.add(ItemReviewMapper.INSTANCE.itemReviewEntityToItemDto(e));
        });

        log.info(this.getClass().getName() + ".findAllReviewByUserId End!");

        return itemReviewDtoList;
    }
}
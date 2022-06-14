package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.jpa.ItemReviewRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import com.submarket.itemservice.mapper.ItemMapper;
import com.submarket.itemservice.mapper.ItemReviewMapper;
import com.submarket.itemservice.service.IItemReviewService;
import com.submarket.itemservice.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@Service(value = "ItemReviewService")
@Slf4j
@RequiredArgsConstructor
public class ItemReviewService implements IItemReviewService {
    private final ItemReviewRepository itemReviewRepository;
    private final ItemService itemService;

    @Override
    public int saveReview(ItemReviewDto itemReviewDto, int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".saveReview Start!");
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        int userAge = Integer.parseInt(itemReviewDto.getUserAge());
        // TODO: 2022-05-16 사용자는 하나의 상품에 하나의 리뷰만 가능하기 때문에 확인 로직, 사용자가 상품을 구독중인지 확인 로직

        ItemDto items = itemService.findItemInfo(itemDto);
        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(items);
        itemReviewDto.setItem(itemEntity);
        // 리뷰 생성 로직

        ItemReviewEntity itemReviewEntity;
        itemReviewEntity = ItemReviewMapper.INSTANCE.itemReviewDtoToItemEntity(itemReviewDto);

        itemService.upCountCustom(itemSeq, userAge, itemReviewDto.getReviewStar()); // readCount += Review Star

        itemReviewRepository.save(itemReviewEntity);


        log.info(this.getClass().getName() + ".saveReview End!");
        return 1;
    }

    @Override
    public int modifyReview(ItemReviewDto itemReviewDto) throws Exception {
        log.info(this.getClass().getName() + ".modifyReview Start!");

        int reviewSeq = itemReviewDto.getReviewSeq();
        String reviewContents = itemReviewDto.getReviewContents();
        String reviewDate = itemReviewDto.getReviewDate();
        int reviewStar = itemReviewDto.getReviewStar();


        itemReviewRepository.modifyItemReview(reviewContents, reviewDate, reviewStar, reviewSeq);

        log.info(this.getClass().getName() + ".modifyReview End!");
        return 1;
    }

    @Override
    public int deleteReview(ItemReviewDto itemReviewDto) throws Exception {
        log.info(this.getClass().getName() + ".deleteReview Start!");

        itemReviewRepository.deleteById(itemReviewDto.getReviewSeq());

        log.info(this.getClass().getName() + ".deleteReview End!");
        return 0;
    }

    @Override
    @Transactional
    public List<ItemReviewDto> findAllReviewInItem(int itemSeq) throws Exception {

        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);
        ItemDto rDto = itemService.findItemInfo(itemDto);

        if (rDto == null) {
            throw new RuntimeException("상품 정보가 없습니다");
        }

        List<ItemReviewDto> reviewDtoList = new LinkedList();

        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(rDto);
        List<ItemReviewEntity> itemReviewEntityList = itemReviewRepository.findByItem(itemEntity);

        itemReviewEntityList.forEach(itemReviewEntity -> {
            reviewDtoList.add(ItemReviewMapper.INSTANCE.itemReviewEntityToItemDto(itemReviewEntity));
        });
        return reviewDtoList;
    }

    @Override
    @Transactional
    public List<ItemReviewDto> findAllReviewByUserId(String userId) throws Exception {
        log.info(this.getClass().getName() + ".findAllReviewByUserId Start!");
        List<ItemReviewDto> itemReviewDtoList = new LinkedList<>();

        List<ItemReviewEntity> itemReviewEntityList = new LinkedList<>();

        itemReviewEntityList = itemReviewRepository.findAllByUserId(CmmUtil.nvl(userId));

        itemReviewEntityList.forEach(e -> {
            itemReviewDtoList.add(ItemReviewMapper.INSTANCE.itemReviewEntityToItemDto(e));
        });

        log.info(this.getClass().getName() + ".findAllReviewByUserId End!");
        return itemReviewDtoList;
    }
}
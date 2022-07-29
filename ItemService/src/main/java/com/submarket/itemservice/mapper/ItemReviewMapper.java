package com.submarket.itemservice.mapper;

import com.submarket.itemservice.dto.ItemReviewDto;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemReviewMapper {
    ItemReviewMapper INSTANCE = Mappers.getMapper(ItemReviewMapper.class);

    @Mapping(target = "item", ignore = true)
    ItemReviewDto itemReviewEntityToItemDto(ItemReviewEntity itemReviewEntity);

    ItemReviewEntity itemReviewDtoToItemEntity(ItemReviewDto itemReviewDto);
}

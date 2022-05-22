package com.submarket.itemservice.mapper;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemEntity itemDtoToItemEntity(ItemDto itemDto);

    @Mapping(target = "category", ignore = true)
    ItemDto itemEntityToItemDto(ItemEntity itemEntity);
}

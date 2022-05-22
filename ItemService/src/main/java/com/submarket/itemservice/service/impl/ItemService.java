package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.dto.GroupDto;
import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.jpa.GroupRepository;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.jpa.entity.GroupEntity;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.mapper.CategoryMapper;
import com.submarket.itemservice.mapper.GroupMapper;
import com.submarket.itemservice.mapper.ItemMapper;
import com.submarket.itemservice.service.IItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service("ItemService")
public class ItemService implements IItemService {
    private final ItemRepository itemRepository;
    private final GroupRepository groupRepository;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public int saveItem(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategorySeq(itemDto.getCategorySeq());

        CategoryDto rDto = categoryService.findCategory(categoryDto);

        log.info("categoryName : " + rDto.getCategoryName());
        CategoryEntity categoryEntity = CategoryMapper.INSTANCE.categoryDtoToCategoryEntity(rDto);

        itemDto.setCategory(categoryEntity);
        itemDto.setItemStatus(1);

        log.info("" + itemDto.getCategory());
        ItemEntity itemEntity = ItemMapper.INSTANCE.itemDtoToItemEntity(itemDto);
        itemRepository.save(itemEntity);

        log.info(this.getClass().getName() + ".saveItem End");
        return 1;
    }
}

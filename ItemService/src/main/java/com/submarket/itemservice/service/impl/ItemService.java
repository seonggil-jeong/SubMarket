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

    @Override
    @Transactional
    public ItemDto findItemInfo(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + "findItemInfo Start!");

        int itemSeq = itemDto.getItemSeq();

        log.info("itemSeq : " + itemSeq);

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);

        ItemDto rDto = ItemMapper.INSTANCE.itemEntityToItemDto(itemEntityOptional.get());

        if (rDto == null) {
            throw new RuntimeException("상품 정보를 찾을 수 없습니다");
        }

        log.info(this.getClass().getName() + ".findItemInfo End!");
        return rDto;
    }

    @Override
    public List<ItemDto> findAllItem() throws Exception {
        log.info(this.getClass().getName() + "findAllItem Start");

        List<ItemDto> itemDtoList = new LinkedList<>();
        Iterable<ItemEntity> itemEntityList = itemRepository.findAll();

        itemEntityList.forEach(itemEntity -> {
            itemDtoList.add(ItemMapper.INSTANCE.itemEntityToItemDto(itemEntity));
        });

        log.info(this.getClass().getName() + "findAllItem End");

        return itemDtoList;
    }

    @Override
    @Transactional
    public int offItem(ItemDto itemDto) throws Exception {
        // TODO: 2022-05-16 상품 활성화 여부 확인
        int itemSeq = itemDto.getItemSeq();

        itemRepository.offItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional
    public int onItem(ItemDto itemDto) throws Exception {
        int itemSeq = itemDto.getItemSeq();

        itemRepository.onItemStatus(itemSeq);
        return 1;
    }

    @Override
    @Transactional
    public int modifyItem(ItemDto itemDto) throws Exception {

        itemRepository.modifyItem(itemDto.getItemSeq(), itemDto.getItemContents(), itemDto.getItemPrice(),
                itemDto.getItemCount(), itemDto.getItemTitle());
        return 1;
    }
}

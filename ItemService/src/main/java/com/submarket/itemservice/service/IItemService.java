package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.GroupDto;
import com.submarket.itemservice.dto.ItemDto;

import java.util.List;

public interface IItemService {
    int saveItem(ItemDto itemDto) throws Exception;

    ItemDto findItemInfo(ItemDto itemDto) throws Exception;

    List<ItemDto> findAllItem() throws Exception;

    int offItem(ItemDto itemDto) throws Exception;

    int onItem(ItemDto itemDto) throws Exception;

    int modifyItem(ItemDto itemDto) throws Exception;

    List<ItemDto> findItemBySellerId(String sellerId) throws Exception;

    void upCount(int itemSeq, int userAge) throws Exception;



}

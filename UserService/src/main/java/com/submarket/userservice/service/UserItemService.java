package com.submarket.userservice.service;

import com.submarket.userservice.dto.ItemDto;
import com.submarket.userservice.dto.LikeDto;

import java.util.List;

public interface UserItemService {
    String itemLikedOrDelete(final String userId, final int itemSeq) throws Exception;

    int likedItemByUserId(final String userId, final int itemSeq) throws Exception;


    List<ItemDto> findAllLikedItems(final String userId) throws Exception;

}

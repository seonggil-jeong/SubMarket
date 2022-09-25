package com.submarket.userservice.service;

import com.submarket.userservice.dto.LikeDto;

public interface UserItemService {
    String itemLikedOrDelete(final String userId, final int itemSeq) throws Exception;

    int likedItemByUserId(final String userId, final int itemSeq) throws Exception;

}

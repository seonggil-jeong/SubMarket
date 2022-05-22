package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.GroupDto;

public interface IGroupService {
    GroupDto findItemInfoByGroupSeq(GroupDto groupDto) throws Exception;
}

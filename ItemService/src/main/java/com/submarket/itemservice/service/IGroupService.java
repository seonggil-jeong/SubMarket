package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.GroupDto;

import java.util.List;

public interface IGroupService {
    GroupDto findItemInfoByGroupSeq(GroupDto groupDto) throws Exception;

    List<GroupDto> findGroupList() throws Exception;
}

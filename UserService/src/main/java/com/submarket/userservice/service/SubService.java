package com.submarket.userservice.service;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;

import java.util.List;

public interface SubService {
    List<SubEntity> findAllSub(SubDto subDto) throws Exception;

    SubDto findOneSub(SubDto subDto) throws Exception;

    int createNewSub(SubDto subDto) throws Exception;

    int updateSub(SubDto subDto);

    int cancelSub(SubDto subDto) throws Exception;

    int findSubCount(List<Integer> itemSeqList) throws Exception;

    int findOneSubCount(int itemSeq) throws Exception;
}

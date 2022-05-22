package com.submarket.userservice.service;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;

import java.util.List;

public interface ISubService {
    int createNewSub(SubDto subDto);

    int updateSub(SubDto subDto);
}

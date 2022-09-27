package com.submarket.userservice.mapper;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubMapper {
    SubMapper INSTANCE = Mappers.getMapper(SubMapper.class);
    @Mapping(target = "user", ignore = true) // Entity 를 DTO 에 담을 때 user 정보 무시
    SubDto subEntityToSubDto(SubEntity subEntity);
    SubEntity subDtoToSubEntity(SubDto subDto);
}

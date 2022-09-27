package com.submarket.sellerservice.mapper;

import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.jpa.entity.SalesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SalesMapper {
    SalesMapper INSTANCE = Mappers.getMapper(SalesMapper.class);
    @Mapping(target = "seller", ignore = true)
    SalesDto salesEntityToSalesDto(SalesEntity salesEntity);
    SalesEntity salesDtoToSalesEntity(SalesDto salesDto);
}

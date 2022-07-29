package com.submarket.sellerservice.mapper;

import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import com.submarket.sellerservice.vo.RequestSellerInfo;
import com.submarket.sellerservice.vo.ResponseSellerInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SellerMapper {
    SellerMapper INSTANCE = Mappers.getMapper(SellerMapper.class);
    @Mapping(source = "sellerEncPassword", target = "sellerPassword")
    SellerEntity SellerDtoToSellerEntity(SellerDto SellerDto);
    SellerDto sellerEntityToSellerDto(SellerEntity sellEntity);
    SellerDto requestSellerInfoToSellerDto(RequestSellerInfo requestSellerInfo);
    ResponseSellerInfo SellerDtoToResponseSellerInfo(SellerDto SellerDto);
}

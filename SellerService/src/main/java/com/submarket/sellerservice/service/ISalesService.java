package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SalesDto;

import java.util.List;

public interface ISalesService {
    int saveSales(SalesDto salesDto, String sellerId) throws Exception;

    List<SalesDto> findAllSalesDtoBySellerId(String sellerId) throws Exception;
}

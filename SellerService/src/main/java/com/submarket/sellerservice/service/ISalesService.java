package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SalesDto;

public interface ISalesService {
    public int saveSales(SalesDto salesDto) throws Exception;
}

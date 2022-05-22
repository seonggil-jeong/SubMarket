package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SellerDto;

public interface ISellerCheckService {
    boolean checkSellerBySellerId(String sellerId) throws Exception;

    boolean checkSellerBySellerEmail(String sellerEmail) throws Exception;

    boolean checkSellerByBusinessId(String businessId) throws Exception;

    boolean checkSellerBySellerPassword(SellerDto SellerDto) throws Exception;
}

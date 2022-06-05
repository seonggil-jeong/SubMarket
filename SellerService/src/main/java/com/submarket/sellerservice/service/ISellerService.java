package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SellerDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ISellerService extends UserDetailsService {

    int createSeller(SellerDto sellerDTO) throws Exception;

    int deleteSeller(SellerDto sellerDTO) throws Exception;

    SellerDto getSellerInfoBySellerEmail(SellerDto sellerDto) throws Exception;

    SellerDto getSellerInfoBySellerId(SellerDto sellerDto) throws Exception;

    SellerDto getSellerDetailsByUserId(String sellerId);

    int changePassword(String oldPassword, String newPassword, String sellerId) throws Exception;

    int modifySellerInfo(SellerDto sellerDto) throws Exception;

}

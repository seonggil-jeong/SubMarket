package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SellerDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ISellerService extends UserDetailsService {
    SellerDto getSellerDetailsByUserId(String sellerId);

}

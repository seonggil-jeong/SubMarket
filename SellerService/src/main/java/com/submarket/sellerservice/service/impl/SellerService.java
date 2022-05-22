package com.submarket.sellerservice.service.impl;

import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.jpa.SellerRepository;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import com.submarket.sellerservice.mapper.SellerMapper;
import com.submarket.sellerservice.service.ISellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service(value = "SellerService")
@RequiredArgsConstructor
@Slf4j
public class SellerService implements ISellerService {
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder passwordEncoder;




    //####################################### JWT Don't change #######################################//
    @Override
    public SellerDto getSellerDetailsByUserId(String sellerId) {
        SellerEntity rEntity = sellerRepository.findBySellerId(sellerId);

        if (rEntity == null) {
            throw new UsernameNotFoundException(sellerId);
        }

        // Status 확인
        if (rEntity.getSellerStatus() == 0) {
            throw new UsernameNotFoundException("탈퇴한 회원");
        }
        SellerDto rDTO = SellerMapper.INSTANCE.sellerEntityToSellerDto(rEntity);

        return rDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String sellerId) throws UsernameNotFoundException {
        log.info("sellerName : " + sellerId);
        SellerEntity rEntity = sellerRepository.findBySellerId(sellerId);

        if (rEntity == null) {
            throw new UsernameNotFoundException(sellerId);
        }

        return new User(rEntity.getSellerId(), rEntity.getSellerPassword(),
                true, true, true, true,
                new ArrayList<>());
    }
}

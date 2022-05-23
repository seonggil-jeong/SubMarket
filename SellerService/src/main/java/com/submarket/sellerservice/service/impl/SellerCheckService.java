package com.submarket.sellerservice.service.impl;

import com.submarket.sellerservice.dto.BusinessIdApiDto;
import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.jpa.SellerRepository;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import com.submarket.sellerservice.service.ISellerCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service("SellerCheckService")
@RequiredArgsConstructor
@Slf4j
public class SellerCheckService implements ISellerCheckService {
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final Environment env;

    @Override // 아이디 중복 확인
    public boolean checkSellerBySellerId(String sellerId) throws Exception {

        SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);

        if (sellerEntity == null) {
            // 중복 없음 실행 가능
            return true;
        }

        return false;
    }

    @Override
    public boolean checkSellerBySellerEmail(String sellerEmail) throws Exception {

        SellerEntity sellerEntity = sellerRepository.findBySellerEmail(sellerEmail);

        if (sellerEntity == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkSellerByBusinessId(String businessId) throws Exception {

        SellerEntity sellerEntity = sellerRepository.findByBusinessId(businessId);

        if (sellerEntity == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkSellerBySellerPassword(SellerDto SellerDto) throws Exception {
        String sellerPassword = SellerDto.getSellerPassword();
        String sellerId = SellerDto.getSellerId();

        SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);
        boolean checkPassword = passwordEncoder.matches(sellerPassword, sellerEntity.getSellerPassword());

        if (checkPassword == false) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        // 비밀번호가 일치하면 True
        return true;
    }

    @Override
    public Map<String, Object> checkBusinessId(String businessId) throws Exception {
        log.info(this.getClass().getName() + "checkBusinessId Start!");

        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", env.getProperty("serviceKey"));

        List<String> bNoList = new LinkedList<>();
        bNoList.add(businessId);

        Map<String, Object> body = new HashMap<>();
        body.put("b_no", bNoList);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<BusinessIdApiDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, BusinessIdApiDto.class);
        BusinessIdApiDto businessIdApiDto = response.getBody();
        Map<String, Object> objectMap = businessIdApiDto.getData().get(0);


        log.info(this.getClass().getName() + "checkBusinessId End!");
        return objectMap;
    }
}

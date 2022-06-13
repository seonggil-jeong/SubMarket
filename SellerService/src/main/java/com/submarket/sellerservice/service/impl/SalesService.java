package com.submarket.sellerservice.service.impl;

import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.jpa.SalesRepository;
import com.submarket.sellerservice.jpa.entity.SalesEntity;
import com.submarket.sellerservice.mapper.SalesMapper;
import com.submarket.sellerservice.service.ISalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesService implements ISalesService {
    private final SalesRepository salesRepository;

    @Override
    @Transactional
    public int saveSales(SalesDto salesDto) throws Exception {
        SalesEntity salesEntity = SalesMapper.INSTANCE.salesDtoToSalesEntity(salesDto);

        salesRepository.save(salesEntity);
        return 0;
    }
}

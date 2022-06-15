package com.submarket.sellerservice.service.impl;

import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.jpa.SalesRepository;
import com.submarket.sellerservice.jpa.SellerRepository;
import com.submarket.sellerservice.jpa.entity.SalesEntity;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import com.submarket.sellerservice.mapper.SalesMapper;
import com.submarket.sellerservice.service.ISalesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SalesService implements ISalesService {
    private final SalesRepository salesRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public int saveSales(SalesDto salesDto, String sellerId) throws Exception {

        String date = salesDto.getDate();
        int totalPrice = salesDto.getValue();

        SellerEntity seller = sellerRepository.findBySellerId(sellerId);
        Optional<SalesEntity> salesEntityBtSeller = salesRepository.findBySellerAndDate(seller, date);

        if (salesEntityBtSeller.isPresent()) {
            int sellerSeq = salesEntityBtSeller.get().getSalesSeq();
            salesRepository.updateSales(totalPrice, sellerSeq, date);

        } else {
            salesDto.setSeller(seller);
            SalesEntity salesEntity = SalesMapper.INSTANCE.salesDtoToSalesEntity(salesDto);
            salesRepository.save(salesEntity);
        }

        return 1;
    }

    @Override
    @Transactional
    public List<SalesDto> findAllSalesDtoBySellerId(String sellerId) throws Exception {

        List<SalesDto> salesDtoList = new LinkedList<>();
        SellerEntity seller = sellerRepository.findBySellerId(sellerId);
        if (seller != null) {
            List<SalesEntity> salesEntityList = salesRepository.findAllBySeller(seller);

            salesEntityList.forEach(salesEntity -> {
                salesDtoList.add(SalesMapper.INSTANCE.salesEntityToSalesDto(salesEntity));
            });

            log.info(this.getClass().getName() + ".findAllSalesDtoBySellerId Start!");
            return salesDtoList;
        } else {

            return salesDtoList;

        }


    }
}

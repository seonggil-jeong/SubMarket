package com.submarket.sellerservice.service.impl;

import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.jpa.SellerRepository;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import com.submarket.sellerservice.mapper.SellerMapper;
import com.submarket.sellerservice.service.ISellerService;
import com.submarket.sellerservice.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service(value = "SellerService")
@RequiredArgsConstructor
@Slf4j
public class SellerService implements ISellerService {
    private final SellerRepository sellerRepository;
    private final SellerCheckService sellerCheckService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;

    /** 사업자 회원가입 */
    @Override
    @Transactional
    public int createSeller(SellerDto SellerDto) throws Exception {
        log.info(this.getClass().getName() + ".createSeller Start!");
        String sellerId = SellerDto.getSellerId();
        String sellerEmail = SellerDto.getSellerEmail();
        String businessId = SellerDto.getBusinessId();

        if (sellerCheckService.checkSellerBySellerId(sellerId)) {
            if (sellerCheckService.checkSellerBySellerEmail(sellerEmail)) {
                if (sellerCheckService.checkSellerByBusinessId(businessId)) {
                    // All pass, 회원가입 로직 실행
                    SellerDto.setSellerEncPassword(passwordEncoder.encode(SellerDto.getSellerPassword()));
                    SellerDto.setSellerStatus(1);
                    SellerEntity sellerEntity = SellerMapper.INSTANCE.SellerDtoToSellerEntity(SellerDto);
                    sellerRepository.save(sellerEntity);

                } else {
                    throw new RuntimeException("사업자 번호 중복");
                }
            } else {
                return 2;
            }
        } else {
            return 3;
        }
        log.info(this.getClass().getName() + ".createSeller End!");

        return 1;
    }

    @Override
    @Transactional
    public int deleteSeller(SellerDto SellerDto) throws Exception {
        log.info(this.getClass().getName() + ".deleteSeller Start!");

        if (sellerCheckService.checkSellerBySellerPassword(SellerDto)) {
            // 일치한다면 진행
            SellerEntity sellerEntity = sellerRepository.findBySellerId(SellerDto.getSellerId());

            if (sellerEntity.getSellerStatus() == 1) {
                // 활성화 되어 있다면 탈퇴, Exception
                sellerRepository.changeSellerStatus(sellerEntity.getSellerSeq());
            } else {
                throw new RuntimeException("이미 탈퇴한 회원입니다");
            }
        } else {
            throw new UsernameNotFoundException("비밀번호 불일치");
        }

        // Kafka
        kafkaProducerService.kafkaDeleteSeller(SellerDto);


        log.info(this.getClass().getName() + ".deleteSeller End!");
        return 1;
    }


    @Override
    @Transactional
    public SellerDto getSellerInfoBySellerEmail(SellerDto sellerDto) throws Exception {
        log.info(this.getClass().getName() + ".getSellerInfoBySellerEmail Start !");
        String sellerEmail = sellerDto.getSellerEmail();

        log.info("sellerEmail : " + sellerEmail);

        SellerEntity sellerEntity = sellerRepository.findBySellerEmail(sellerEmail);

        if (sellerEntity == null) {
            throw new RuntimeException("사용자 정보를 찾을 수 없습니다");
        }

        log.info(this.getClass().getName() + ".getSellerInfoBySellerEmail End !");
        SellerDto rDto = SellerMapper.INSTANCE.sellerEntityToSellerDto(sellerEntity);

        return rDto;
    }

    @Override
    @Transactional
    public SellerDto getSellerInfoBySellerId(SellerDto sellerDto) throws Exception {
        log.info(this.getClass().getName() + "getSellerInfoBySellerId Start!");
        String sellerId = sellerDto.getSellerId();

        SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);

        SellerDto rDto = SellerMapper.INSTANCE.sellerEntityToSellerDto(sellerEntity);


        log.info(this.getClass().getName() + "getSellerInfoBySellerId End!");

        return rDto;
    }

    @Override
    @Transactional // 비밀번호 변경
    public int changePassword(String oldPassword, String newPassword, String sellerId) throws Exception {
        log.info(this.getClass().getName() + ".changePassword Start!");
        int res = 0;
        SellerEntity sellerEntity = sellerRepository.findBySellerId(sellerId);
        SellerDto sellerDto = new SellerDto();
        // 인코딩된 Password
        sellerDto.setSellerId(sellerId);
        sellerDto.setSellerPassword(oldPassword);

        if (sellerCheckService.checkSellerBySellerPassword(sellerDto)) {
            // 비밀번호가 일치한다면 변경 실행
            sellerRepository.changeSellerPassword(passwordEncoder.encode(newPassword), sellerId);
            res = 1;
        }

        log.info(this.getClass().getName() + ".changePassword End!");
        return res;
    }

    @Override
    @Transactional
    public int modifySellerInfo(SellerDto sellerDto) throws Exception {
        log.info(this.getClass().getName() + ".modifySellerInfo Start!");
        String sellerId = CmmUtil.nvl(sellerDto.getSellerId());
        String sellerHome = CmmUtil.nvl(sellerDto.getSellerHome());
        String sellerAddress = CmmUtil.nvl(sellerDto.getSellerAddress());
        String sellerAddress2 = CmmUtil.nvl(sellerDto.getSellerAddress2());

        sellerRepository.modifySellerInfo(sellerHome, sellerAddress, sellerAddress2, sellerId);

        log.info(this.getClass().getName() + ".modifySellerInfo End!");
        return 1;
    }

    //####################################### JWT Don't change #######################################//
    @Override
    @Transactional
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
    @Transactional
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

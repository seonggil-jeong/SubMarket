package com.submarket.userservice.service.impl;

import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubCheckServiceImpl implements com.submarket.userservice.service.SubCheckService {
    private final SubRepository subRepository;
    private final UserRepository userRepository;

    @Override
    public boolean SubCheck(Integer subSeq) {
        log.info(this.getClass().getName() + "subCheck");

        Optional<SubEntity> subEntityOptional = subRepository.findById(subSeq);

        if (subEntityOptional.isPresent()) {
            return true;
        }

        return false;

    }

    @Override // 사용자가 이미 같은 상품을 구독하고 있는지 조회
    @Transactional
    public boolean checkHasSubByItemSeqAndUserId(Integer itemSeq, String userId) throws Exception {
        log.info(this.getClass().getName() + ".checkHasSubByItemSeq Start!");
        boolean res = false;

        try {
            UserEntity user = userRepository.findByUserId(userId);

            SubEntity subEntity = subRepository.findByItemSeqAndUser(itemSeq, user);

            if (subEntity == null) {
                // 중복 X
                res = true;
            } else {
                // 중복
                log.info("중복 구독");
                res = false;
            }

        } catch (Exception exception) {
            log.info("Exception : " + exception);
            res = false;
        } finally {
            log.info(this.getClass().getName() + ".checkHasSubByItemSeq End!");
            return res;

        }





    }
}

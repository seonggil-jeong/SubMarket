package com.submarket.userservice.service.impl;

import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.service.ISubCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubCheckService implements ISubCheckService {
    private final SubRepository subRepository;

    @Override
    public boolean SubCheck(Integer subSeq) {
        log.info(this.getClass().getName() + "subCheck");

        Optional<SubEntity> subEntityOptional = subRepository.findById(subSeq);

        if (subEntityOptional.isPresent()) {
            return true;
        }

        return false;

    }
}

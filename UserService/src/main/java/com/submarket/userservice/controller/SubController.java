package com.submarket.userservice.controller;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.mapper.SubMapper;
import com.submarket.userservice.service.impl.SubService;
import com.submarket.userservice.vo.RequestSub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class SubController {
    private final SubService subService;

    @PostMapping("/sub")
    public ResponseEntity<String> createNewSub(@RequestBody RequestSub requestSub) {
        log.info(this.getClass().getName() + ".createNewSub Start!");

        SubDto subDto = new SubDto();
        subDto.setItemSeq(requestSub.getItemSeq());

        int res = subService.createNewSub(subDto);

        if (res != 1) {
            return ResponseEntity.status(500).body("오류");
        }

        log.info(this.getClass().getName() + ".createNewSub End! ");

        return ResponseEntity.status(HttpStatus.CREATED).body("구독 성공");
    }
}

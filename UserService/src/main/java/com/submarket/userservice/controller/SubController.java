package com.submarket.userservice.controller;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.mapper.SubMapper;
import com.submarket.userservice.service.impl.SubService;
import com.submarket.userservice.util.TokenUtil;
import com.submarket.userservice.vo.RequestSub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
public class SubController {
    private final SubService subService;
    private final TokenUtil tokenUtil;

    @GetMapping("/sub")
    public ResponseEntity<Object> findAllSub(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".findSub Start!");

        String userId = tokenUtil.getUserIdByToken(headers);


        SubDto subDto = new SubDto();
        subDto.setUserId(userId);
        List<SubEntity> subEntityList = subService.findAllSub(subDto);

        if (subEntityList == null) {
            log.info("SubService Check");

            return ResponseEntity.status(500).body("Service Error");
        }

        List<SubDto> subDtoList = new ArrayList<>();

        subEntityList.forEach(subEntity -> {
            subDtoList.add(SubMapper.INSTANCE.subEntityToSubDto(subEntity));
        });

        return ResponseEntity.ok().body(subEntityList);



    }

    @GetMapping("/sub/{subSeq}")
    public ResponseEntity<SubDto> findOneSub(@PathVariable int subSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneSub Start!");
        SubDto pDto = new SubDto();

        pDto.setSubSeq(subSeq);

        SubDto subDto = subService.findOneSub(pDto);

        if (subDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        log.info(this.getClass().getName() + ".findOneSub Start!");

        return ResponseEntity.ok().body(subDto);
    }

    @PostMapping("/sub")
    public ResponseEntity<String> createNewSub(@RequestHeader HttpHeaders headers,
                                               @RequestBody RequestSub requestSub) {
        log.info(this.getClass().getName() + ".createNewSub Start!");

        SubDto subDto = new SubDto();
        String userId = tokenUtil.getUserIdByToken(headers);

        subDto.setItemSeq(requestSub.getItemSeq());
        subDto.setUserId(userId);

        int res = subService.createNewSub(subDto);

        if (res != 1) {
            return ResponseEntity.status(500).body("오류");
        }

        log.info(this.getClass().getName() + ".createNewSub End! ");

        return ResponseEntity.status(HttpStatus.CREATED).body("구독 성공");
    }

    @DeleteMapping("/sub")
    public String cancelSub(@RequestBody RequestSub requestSub) throws Exception {
        log.info(this.getClass().getName() + "cancel Sub Start!");

        SubDto subDto = new SubDto();

        subDto.setSubSeq(requestSub.getSubSeq());

        int res = subService.cancelSub(subDto);


        log.info(this.getClass().getName() + "cancel Sub End!");

        if (res != 1) {
            return "구독 취소 실패";
        }
        return "구독 취소 성공";
    }

    @PatchMapping("/sub")
    public ResponseEntity<String> updateSub(@RequestBody RequestSub requestSub) throws Exception {
        log.info(this.getClass().getName() + ".updateSub Start!");
        SubDto subDto = new SubDto();
        subDto.setSubSeq(requestSub.getSubSeq());

        int res = subService.updateSub(subDto);

        if (res != 1) {
            return ResponseEntity.ok("갱신 실패");
        }

        log.info(this.getClass().getName() + "updateSub End!");
        return ResponseEntity.ok("갱신 완료");


    }

    @GetMapping("/seller/sub")
    public ResponseEntity<Integer> findSubCount(@RequestBody Map<String, Object> request)  throws Exception {
        // Seller 가 보유하고 있는 상품의 SeqList 를 넘겨주면 총 구독 수를 표시
        log.info(this.getClass().getName() + "findSubCount");
        List<Integer> itemSeqList = new LinkedList<>();
        itemSeqList = (List<Integer>) request.get("itemSeqList");

        int count = subService.findSubCount(itemSeqList);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }
}

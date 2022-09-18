package com.submarket.userservice.service.impl;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerServiceImpl {
    /**
     * 특정 시간에 실행 Batch, and if (day == new Date()) == 결제 + 구독 정보 업데이트 로직 실행
     */
    private final SubServiceImpl subServiceImpl;
    private final SubRepository subRepository;

    //    @Scheduled // 특정 시간에 실행
    @Scheduled(cron = "* * 9 * * *") // s, m, h, d, M (매일 9시에 구독 확인)
    @Async
    public void checkSub() throws Exception {
        log.info(this.getClass().getName() + ".checkSub Start!");

        Iterable<SubEntity> subEntityIterable = subRepository.findUpdateSub(DateUtil.getDateTime("dd"));
        // 오늘 날짜에 맞는 구독 정보 조회 ex.) 01, 02, 03

        if (subEntityIterable == null) {
            // 갱신할 내용이 없다면
            return;
        }

        SubDto subDto = new SubDto();

        subEntityIterable.forEach(subEntity -> {
            subDto.setSubSeq(subEntity.getSubSeq());

            // 구독 업데이트
            subServiceImpl.updateSub(subDto);
        });


        log.info(this.getClass().getName() + ".checkSub End!");

    }
}

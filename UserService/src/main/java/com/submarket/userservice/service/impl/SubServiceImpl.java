package com.submarket.userservice.service.impl;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.exception.SubException;
import com.submarket.userservice.exception.result.SubExceptionResult;
import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.mapper.SubMapper;
import com.submarket.userservice.util.CmmUtil;
import com.submarket.userservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service(value = "SubService")
@Slf4j
@RequiredArgsConstructor
public class SubServiceImpl implements com.submarket.userservice.service.SubService {
    private final SubRepository subRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;
    private final SubCheckServiceImpl subCheckServiceImpl;
    private final MailServiceImpl mailServiceImpl;
    private final KafkaProducerServiceImpl kafkaProducerServiceImpl;

    /** ------------------------- 구독 조회 ------------------------------*/
    @Override
    @Transactional
    public List<SubEntity> findAllSub(SubDto subDto) throws RuntimeException{
        UserEntity user = userRepository.findByUserId(subDto.getUserId());

        if (user == null) {
            log.info("User 정보 없음");
            throw new EntityNotFoundException("User Entity Not Found");
        }
        Iterable<SubEntity> subEntityIterable = subRepository.findByUser(user);

        log.info(this.getClass().getName() + ". find Sub Info");

        List<SubEntity> subEntityList = new ArrayList<>();

        subEntityIterable.forEach(e -> {
            subEntityList.add(e);
        });
        return subEntityList;
    }


    @Override // 구독 상세 조회
    @Transactional(rollbackOn = Exception.class)
    public SubDto findOneSub(SubDto subDto) throws Exception {
        log.info(this.getClass().getName() + "findOne Sub Start!");
        int subSeq = subDto.getSubSeq();
        SubDto pDto = new SubDto();

        Optional<SubEntity> subEntityOptional = subRepository.findById(subSeq);

        SubEntity subEntity = subEntityOptional.get();
        log.info("subSeq : " + subEntity.getSubSeq());
        log.info("itemSeq : " + subEntity.getItemSeq());
        log.info("subDate : " + subEntity.getSubDate());
        log.info("subCount : " + subEntity.getSubCount());

        if (subEntity == null) {
            throw new SubException(SubExceptionResult.CAN_NOT_FOUND_SUB_INFO);
        }
        pDto = SubMapper.INSTANCE.subEntityToSubDto(subEntity);


        return pDto;
    }

    /** ------------------------- 구독 생성 ------------------------------*/
    @Override
    @Transactional
    public int createNewSub(SubDto subDto) throws Exception{
        log.info(this.getClass().getName() + "createNewSub Start!");

        int res = 0;
        UserEntity user = userRepository.findByUserId(subDto.getUserId());
        subDto.setUser(user);

        // Default Setting
        subDto.setSubDate(DateUtil.getDateTime("dd"));
        subDto.setSubCount(1);
        subDto.setUserAge(CmmUtil.nvl(user.getUserAge()));

        log.info("itemSeq : " + subDto.getItemSeq());

        // 이미 구독 여부 확인
        if (subCheckServiceImpl.checkHasSubByItemSeqAndUserId(subDto.getItemSeq(), subDto.getUserId())) {
            SubEntity subEntity = SubMapper.INSTANCE.subDtoToSubEntity(subDto);
            log.info("subEntity (itemSeq) : " + subEntity.getItemSeq());
            subRepository.save(subEntity);
            mailServiceImpl.sendMail(subDto.getUser().getUserEmail(), "구독 성공", subDto.getUser().getUserName() + "님 구독이 완료 됐습니다!!");

            // kafka (구독 성공 시 Item Count - 1)

            kafkaProducerServiceImpl.createNewSub(subDto);

            res = 1;
        } else {
            res = 2; // 중복  = 2
        }

        log.info(this.getClass().getName() + "createNewSub End");

        return res;

    }

    /** ------------------------- 구독 갱신 ------------------------------*/
    @Override
    @Transactional
    public int updateSub(SubDto subDto) {
        log.info(this.getClass().getName() + ".updateSub Start!");

        String date = DateUtil.getDateTime(DateUtil.getDateTime("dd"));
        int res = subRepository.updateSub(date, subDto.getSubSeq());


        log.info(this.getClass().getName() + ".updateSub End!");
        return res;
    }

    /** ------------------------- 구독 취소 ------------------------------*/
    @Override
    @Transactional
    public int cancelSub(SubDto subDto) throws Exception{
        log.info(this.getClass().getName() + ".cancelSub Start!");
        if (subCheckServiceImpl.SubCheck(subDto.getSubSeq())) {

            Optional<SubEntity> subEntity = subRepository.findById(subDto.getSubSeq());
            subDto.setItemSeq(subEntity.get().getItemSeq());

            // not null 삭제 실행
            subRepository.deleteById(subDto.getSubSeq());

            kafkaProducerServiceImpl.cancelSub(subDto);

        } else {
            log.info("구독 정보 찾기 실패");
            throw new RuntimeException("구독 정보를 찾을 수 없습니다");
        }

        log.info(this.getClass().getName() + "cancelSub End!");
        return 1;
    }

    @Override
    public int findSubCount(List<Integer> itemSeqList) throws Exception {
        log.info(this.getClass().getName() + "findSubCount");
        int count = 0;
        try {
            for (Integer itemSeq : itemSeqList) {
                List<SubEntity> subEntityList = subRepository.findAllByItemSeq(itemSeq);
                count += subEntityList.size();
            }

        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info( code + "HttpStatusCodeException : " + statusCodeException);

        } catch (Exception e) {
            log.info("Exception : " + e);

        } finally {
            return count;
        }
    }

    @Override
    public int findOneSubCount(int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneSubCount Start!");
        int res = 0;

        List<SubEntity> subEntityList = subRepository.findAllByItemSeq(itemSeq);

        if (subEntityList.size() > 0) {
            res += subEntityList.size();
        }

        log.info(this.getClass().getName() + ".findOneSubCount End!");
        return res;
    }
}

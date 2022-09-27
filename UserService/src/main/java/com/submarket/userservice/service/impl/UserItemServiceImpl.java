package com.submarket.userservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.userservice.client.ItemServiceClient;
import com.submarket.userservice.dto.ItemDto;
import com.submarket.userservice.dto.LikeDto;
import com.submarket.userservice.exception.ItemException;
import com.submarket.userservice.exception.UserException;
import com.submarket.userservice.exception.result.ItemExceptionResult;
import com.submarket.userservice.exception.result.UserExceptionResult;
import com.submarket.userservice.jpa.LikeRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.LikeEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.service.KafkaProducerService;
import com.submarket.userservice.service.UserItemService;
import com.submarket.userservice.vo.ItemInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ItemServiceClient itemServiceClient;

    private final CircuitBreakerFactory circuitBreakerFactory;


    /**
     * 상품 좋아요 및 좋아요 취소 -> 값이 있다면 delete or create
     *
     * @param userId
     * @param itemSeq
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public String itemLikedOrDelete(final String userId, final int itemSeq) throws Exception {
        log.debug("item Liked Start!");
        Optional<UserEntity> user = Optional.of(userRepository.findByUserId(userId));

        // 상품 유효성 검사
        log.info("Before call checking item");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("itemCircuit");
        ItemDto itemDto = circuitBreaker.run(() -> itemServiceClient.isItem(itemSeq).getBody(),
                throwable -> ItemDto.builder().itemSeq(-1).build());

        log.info("After call checking item");
        log.info("itemSeq : " + itemDto.getItemSeq());


        if (itemDto == null) {
            throw new ItemException(ItemExceptionResult.NOT_MATCHED_ITEM_SEQ);
        }

        Optional<LikeEntity> result = likeRepository.findByUserAndItemSeq(user
                        .orElseThrow(() -> new UserException(UserExceptionResult.USER_NOT_FOUNT))
                , itemSeq);


        if (result.isPresent()) { // 있을 경우 삭제

            likeRepository.deleteById(result.get().getLikeSeq());

            kafkaProducerService.cancelItemLike(LikeDto.builder().itemSeq(itemSeq).userId(userId).build());

            return "좋아요 취소 성공";

        } else { // 없을 경우 생성

            likeRepository.save(LikeEntity.builder()
                    .user(user.get())
                    .itemSeq(itemSeq).build());

            kafkaProducerService.createItemLike(LikeDto.builder().itemSeq(itemSeq).userId(userId).build());


            return "좋아요 성공";
        }
    }

    /**
     * 상품 좋아요 유무를 확인
     *
     * @param userId  사용자 아이디
     * @param itemSeq 상품 번호
     * @return 1, 0
     * @throws Exception
     */
    @Override
    public int likedItemByUserId(String userId, int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".likedItemByUserId Start!");

        UserEntity user = userRepository.findByUserId(userId);

        Optional<LikeEntity> result = likeRepository.findByUserAndItemSeq(user, itemSeq);

        log.info(this.getClass().getName() + ".likedItemByUserId End!");
        // 일치하는 정보가 있을 경우 1 상품 좋아요 유 , Else 0 좋아요 무
        return result.isPresent() ? 1 : 0;
    }


    /**
     * 사용자가 좋아요한 상품 목록 조회
     *
     * @param userId 사용자 아이디
     * @return 좋아요한 상품 목록
     * @throws Exception
     */
    @Override
    public List<ItemDto> findAllLikedItems(String userId) throws Exception {
        log.info("Service Start!");

        List<ItemDto> result = new LinkedList<>();
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) { // 사용자 정보를 찾을 수 없음
            throw new UserException(UserExceptionResult.USER_NOT_FOUNT);
        }

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("findItemInfo");
        Optional<List<LikeEntity>> likeEntities = likeRepository.findAllByUser(user);

        if (likeEntities.isPresent()) { // 값이 있다면 상품 정보 조회 시작
            likeEntities.get().forEach(likeEntity -> {
                ItemInfoResponse response = circuitBreaker.run(() -> itemServiceClient.findOneItem(likeEntity.getItemSeq()).getBody(),
                        throwable -> new ItemInfoResponse());

                result.add(new ObjectMapper().convertValue(response, ItemDto.class));
            });
        }

        log.info("Service End!");
        return result;
    }
}

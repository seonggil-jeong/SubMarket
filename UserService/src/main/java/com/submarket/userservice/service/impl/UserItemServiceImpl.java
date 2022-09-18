package com.submarket.userservice.service.impl;

import com.submarket.userservice.dto.LikeDto;
import com.submarket.userservice.exception.UserException;
import com.submarket.userservice.exception.result.UserExceptionResult;
import com.submarket.userservice.jpa.LikeRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.LikeEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.service.KafkaProducerService;
import com.submarket.userservice.service.UserItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserItemServiceImpl implements UserItemService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;


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

        // select Item from Like where user AND itemSeq
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
}

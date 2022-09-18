package com.submarket.userservice.jpa;

import com.submarket.userservice.jpa.entity.LikeEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {


    Optional<LikeEntity> findByUserAndItemSeq(UserEntity user, int itemSeq);
}

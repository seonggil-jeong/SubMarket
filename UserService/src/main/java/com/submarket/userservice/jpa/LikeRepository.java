package com.submarket.userservice.jpa;

import com.submarket.userservice.jpa.entity.LikeEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends CrudRepository<LikeEntity, Integer> {


    Optional<LikeEntity> findByUserAndItemSeq(UserEntity user, int itemSeq);

    Optional<List<LikeEntity>> findAllByUser(UserEntity user);

}

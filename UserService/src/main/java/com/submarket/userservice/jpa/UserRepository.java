package com.submarket.userservice.jpa;

import com.submarket.userservice.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    // 사용자 ID로 사용자 정보 조회
    @Transactional
    UserEntity findByUserId(String userId);

    // 사용자 Email 로 사용자 정보 조회
    @Transactional
    UserEntity findByUserEmail(String userEmail);

    // 비밀번호 변경
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_info set user_password = :password WHERE user_seq = :userSeq", nativeQuery = true)
    void changeUserPassword(@Param("password") String password, @Param("userSeq") int userSeq);



}

package com.submarket.userservice.jpa;

import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubRepository extends CrudRepository<SubEntity, Integer> {

    // 구독 갱신 (Count += 1, Date 변경)
    @Transactional
    @Modifying
    @Query(value = "UPDATE sub_info set sub_count = sub_count + 1, sub_date = :subDate where sub_seq = :subSeq", nativeQuery = true)
    int updateSub(@Param("subDate") String subDate, @Param("subSeq") int subSeq);

    List<SubEntity> findByUser(UserEntity user);

    @Transactional
    @Query(value = "SELECT * FROM sub_info WHERE sub_date = :subDate", nativeQuery = true)
    Iterable<SubEntity> findUpdateSub(String subDate);
}

package com.submarket.itemservice.jpa;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemReviewRepository extends JpaRepository<ItemReviewEntity, Integer> {

    @Override
    @Transactional
    Optional<ItemReviewEntity> findById(Integer integer);

    @Transactional
    Optional<ItemReviewEntity> findByUserIdAndItem(String userId, ItemEntity item);

    @Transactional
    List<ItemReviewEntity> findByItem(ItemEntity item);

    @Transactional
    List<ItemReviewEntity> findAllByUserId(String userId);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ITEM_REVIEW SET reviewContents = :reviewContents, reviewDate = :reviewDate, reviewStar = :reviewStar WHERE reviewSeq = :reviewSeq", nativeQuery = true)
    void modifyItemReview(@Param("reviewContents") String reviewContents, @Param("reviewDate") String reviewDate, @Param("reviewStar") int reviewStar, @Param("reviewSeq") int reviewSeq);

}

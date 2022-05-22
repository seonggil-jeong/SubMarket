package com.submarket.itemservice.jpa;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemReviewRepository extends CrudRepository<ItemReviewEntity, Integer> {

    @Override
    @Transactional
    Optional<ItemReviewEntity> findById(Integer integer);

    @Transactional
    List<ItemReviewEntity> findByItem(ItemEntity item);

    @Transactional
    @Modifying
    @Query(value = "UPDATE item_review_info SET review_contents = :reviewContents, review_date = :reviewDate, review_star = :reviewStar WHERE review_seq = :reviewSeq", nativeQuery = true)
    void modifyItemReview(@Param("reviewContents") String reviewContents, @Param("reviewDate") String reviewDate, @Param("reviewStar") int reviewStar, @Param("reviewSeq") int reviewSeq);

}

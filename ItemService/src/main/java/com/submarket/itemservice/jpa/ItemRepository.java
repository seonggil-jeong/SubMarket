package com.submarket.itemservice.jpa;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
    @Override
    @Transactional
    Optional<ItemEntity> findById(Integer integer);

    @Transactional
    List<ItemEntity> findAllBySellerId(String sellerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE item_info SET item_status = 0 WHERE item_seq = :itemSeq", nativeQuery = true)
    int offItemStatus(@Param("itemSeq") int itemSeq);

    @Modifying
    @Transactional
    @Query(value = "UPDATE item_info SET item_status = 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    int onItemStatus(@Param("itemSeq") int itemSeq);

    @Modifying
    @Transactional
    @Query(value = "UPDATE item_info SET item_contents = :itemContents, item_price = :itemPrice, item_count = :itemCount," +
            "item_title = :itemTitle WHERE item_seq = :itemSeq", nativeQuery = true)
    int modifyItem(@Param("itemSeq") int itemSeq, @Param("itemContents") String itemContents, @Param("itemPrice") int itemPrice,
                   @Param("itemCount") int itemCount, @Param("itemTitle") String itemTitle);

    @Modifying
    @Transactional
    @Query(value = "UPDATE item_info SET item_count = item_count - 1 WHERE itemSeq = :itemSeq", nativeQuery = true)
    void reduceItemCount(@Param("itemSeq") int itemSeq);
}

package com.submarket.itemservice.jpa;

import com.submarket.itemservice.jpa.entity.ItemEntity;
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
public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    @Override
    Optional<ItemEntity> findById(Integer integer);

    List<ItemEntity> findAllBySellerId(String sellerId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET itemStatus = 0 WHERE item_seq = :itemSeq", nativeQuery = true)
    int offItemStatus(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET itemStatus = 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    int onItemStatus(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET itemContents = :itemContents, itemPrice = :itemPrice, itemCount = :itemCount," +
            "itemTitle = :itemTitle WHERE item_seq = :itemSeq", nativeQuery = true)
    int modifyItem(@Param("itemSeq") int itemSeq, @Param("itemContents") String itemContents, @Param("itemPrice") int itemPrice,
                   @Param("itemCount") int itemCount, @Param("itemTitle") String itemTitle);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET itemCount = itemCount - 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void reduceItemCount(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET itemCount = itemCount + 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseItemCount(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount20 = readCount20 + 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseReadCount20(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount30 = readCount30 + 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseReadCount30(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount40 = readCount40 + 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseReadCount40(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount_other = readCountOther + 1 WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseReadCountOther(@Param("itemSeq") int itemSeq);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount20 = readCount20 + :readValue WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseCustomReadCount20(@Param("itemSeq") int itemSeq, @Param("readValue") int readValue);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount30 = readCount30 + :readValue WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseCustomReadCount30(@Param("itemSeq") int itemSeq, @Param("readValue") int readValue);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCount40 = readCount40 + :readValue WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseCustomReadCount40(@Param("itemSeq") int itemSeq, @Param("readValue") int readValue);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE ITEM SET readCountOther = readCountOther + :readValue WHERE item_seq = :itemSeq", nativeQuery = true)
    void increaseCustomReadCountOther(@Param("itemSeq") int itemSeq, @Param("readValue") int readValue);
}

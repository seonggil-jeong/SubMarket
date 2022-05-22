package com.submarket.sellerservice.jpa;

import com.submarket.sellerservice.jpa.entity.SellerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface SellerRepository extends CrudRepository<SellerEntity, Integer> {
    SellerEntity findBySellerId(String sellerId);

    SellerEntity findBySellerEmail(String sellerEmail);

    SellerEntity findByBusinessId(String businessId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE seller_info SET seller_status = 0 WHERE seller_seq = :sellerSeq", nativeQuery = true)
    void changeSellerStatus(int sellerSeq);
}

package com.submarket.sellerservice.jpa;

import com.submarket.sellerservice.jpa.entity.SellerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface SellerRepository extends CrudRepository<SellerEntity, Integer> {
    SellerEntity findBySellerId(String sellerId);

    SellerEntity findBySellerEmail(String sellerEmail);

    SellerEntity findByBusinessId(String businessId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE seller_info SET seller_status = 0 WHERE seller_seq = :sellerSeq", nativeQuery = true)
    void changeSellerStatus(@Param("sellerSeq") int sellerSeq);

    @Transactional
    @Modifying
    @Query(value = "UPDATE seller_info SET seller_password = :sellerPassword WHERE seller_id = :sellerId", nativeQuery = true)
    void changeSellerPassword(@Param("sellerPassword") String sellerPassword, @Param("sellerId") String sellerId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE seller_info SET seller_home = :sellerHome, seller_address = :sellerAddress, seller_address2 = :sellerAddress2 WHERE seller_id = :sellerId", nativeQuery = true)
    void modifySellerInfo(@Param("sellerHome") String sellerHome, @Param("sellerAddress") String sellerAddress,
                          @Param("sellerAddress2") String sellerAddress2, @Param("sellerId") String sellerId);
}

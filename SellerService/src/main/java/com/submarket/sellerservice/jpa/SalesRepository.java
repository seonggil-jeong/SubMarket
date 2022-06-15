package com.submarket.sellerservice.jpa;

import com.submarket.sellerservice.jpa.entity.SalesEntity;
import com.submarket.sellerservice.jpa.entity.SellerEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends CrudRepository<SalesEntity, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE sales_info SET value = value + :totalPrice WHERE seller_seller_seq = :sellerSeq AND date = :date", nativeQuery = true)
    void updateSales(@Param("totalPrice") int  totalPrice, @Param("sellerSeq") int sellerSeq, @Param("date") String date);

    @Transactional
    Optional<SalesEntity> findBySellerAndDate(SellerEntity seller, String date);

    @Transactional
    List<SalesEntity> findAllBySeller(SellerEntity seller);

}

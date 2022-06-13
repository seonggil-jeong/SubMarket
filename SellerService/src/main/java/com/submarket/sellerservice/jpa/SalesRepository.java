package com.submarket.sellerservice.jpa;

import com.submarket.sellerservice.jpa.entity.SalesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends CrudRepository<SalesEntity, Integer> {

}

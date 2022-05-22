package com.submarket.orderservice.mapper.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.submarket.orderservice.config.AbstractMongoDBComon;
import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.mapper.IOrderMapper;
import com.submarket.orderservice.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component(value = "OrderMapper")
public class OrderMapper extends AbstractMongoDBComon implements IOrderMapper {

    @Override
    public int insertOrder(OrderDto orderDto, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".insertOrder Start!");

        int res = 0;

        super.createCollection(colNm);

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        col.insertOne(new Document(new ObjectMapper().convertValue(orderDto, Map.class)));

        res = 1;

        log.info(this.getClass().getName() + "insertOrder End!");

        return res;
    }
}

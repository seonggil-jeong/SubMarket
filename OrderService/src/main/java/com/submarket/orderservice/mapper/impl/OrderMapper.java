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
import org.springframework.web.client.HttpStatusCodeException;

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

    @Override
    public List<OrderDto> findOrderInfoByUserId(String userId, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByUserId Start!");
        List<OrderDto> orderDtoList = new LinkedList<>();
        try {
            MongoCollection<Document> col = mongodb.getCollection(colNm);

            Document query = new Document();
            query.append("userId", userId);

            Document projection = new Document();
            projection.append("orderId", "$orderId");
            projection.append("orderDate", "$orderDate");
            projection.append("itemSeq", "$itemSeq");
            projection.append("sellerId", "$sellerId");
            projection.append("userAddress", "$userAddress");
            projection.append("userAddress2", "$userAddress2");
            projection.append("orderDateDetails", "$orderDateDetails");
            projection.append("_id", 0);

            FindIterable<Document> rs = col.find(query).projection(projection);

            for (Document doc : rs) {
                if (doc == null) {
                    doc = new Document();
                }

                String orderId = CmmUtil.nvl(doc.getString("orderId"));
                String orderDate = CmmUtil.nvl(doc.getString("orderDate"));
                int itemSeq = doc.getInteger("itemSeq");
                String sellerId = CmmUtil.nvl(doc.getString("sellerId"));
                String userAddress = CmmUtil.nvl(doc.getString("userAddress"));
                String userAddress2 = CmmUtil.nvl(doc.getString("userAddress2"));
                String orderDateDetails = CmmUtil.nvl(doc.getString("orderDateDetails"));

                OrderDto orderDto = new OrderDto();

                orderDto.setOrderId(orderId);
                orderDto.setOrderDate(orderDate);
                orderDto.setItemSeq(itemSeq);
                orderDto.setSellerId(sellerId);
                orderDto.setUserAddress(userAddress);
                orderDto.setUserAddress2(userAddress2);
                orderDto.setOrderDateDetails(orderDateDetails);

                orderDtoList.add(orderDto);
            }
        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info(code + "(HttpStatusCodeException) : " + statusCodeException);
            orderDtoList = new LinkedList<>();
        } catch (Exception e) {
            log.info("Exception : " + e);
            orderDtoList = new LinkedList<>();
        } finally {
            return orderDtoList;
        }
    }

    @Override
    public List<OrderDto> findOrderInfoBySellerId(String sellerId, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByUserId Start!");
        List<OrderDto> orderDtoList = new LinkedList<>();
        try {
            MongoCollection<Document> col = mongodb.getCollection(colNm);

            Document query = new Document();
            query.append("sellerId", sellerId);

            Document projection = new Document();

            projection.append("orderId", "$orderId");
            projection.append("orderDate", "$orderDate");
            projection.append("itemSeq", "$itemSeq");
            projection.append("userId", "$userId");
            projection.append("userAddress", "$userAddress");
            projection.append("userAddress2", "$userAddress2");
            projection.append("orderDateDetails", "$orderDateDetails");

            projection.append("_id", 0);

            FindIterable<Document> rs = col.find(query).projection(projection);

            for (Document doc : rs) {
                if (doc == null) {
                    doc = new Document();
                }

                String orderId = CmmUtil.nvl(doc.getString("orderId"));
                String orderDate = CmmUtil.nvl(doc.getString("orderDate"));
                int itemSeq = doc.getInteger("itemSeq");
                String userId = CmmUtil.nvl(doc.getString("userId"));
                String userAddress = CmmUtil.nvl(doc.getString("userAddress"));
                String userAddress2 = CmmUtil.nvl(doc.getString("userAddress2"));
                String orderDateDetails = CmmUtil.nvl(doc.getString("orderDateDetails"));

                OrderDto orderDto = new OrderDto();

                orderDto.setOrderId(orderId);
                orderDto.setOrderDate(orderDate);
                orderDto.setItemSeq(itemSeq);
                orderDto.setSellerId(sellerId);
                orderDto.setUserAddress(userAddress);
                orderDto.setUserAddress2(userAddress2);
                orderDto.setOrderDateDetails(orderDateDetails);
                orderDtoList.add(orderDto);
            }
        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info(code + "(HttpStatusCodeException) : " + statusCodeException);
            orderDtoList = new LinkedList<>();
        } catch (Exception e) {
            log.info("Exception : " + e);
            orderDtoList = new LinkedList<>();
        } finally {
            return orderDtoList;
        }
    }

    @Override
    public OrderDto findOrderInfoByOrderId(String orderId, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByUserId Start!");
        OrderDto orderDto = new OrderDto();
        try {
            MongoCollection<Document> col = mongodb.getCollection(colNm);

            Document query = new Document();
            query.append("orderId", orderId);

            Document projection = new Document();

            projection.append("orderId", "$orderId");
            projection.append("orderDate", "$orderDate");
            projection.append("itemSeq", "$itemSeq");
            projection.append("userId", "$userId");
            projection.append("sellerId", "$sellerId");
            projection.append("userAddress", "$userAddress");
            projection.append("userAddress2", "$userAddress2");
            projection.append("orderDateDetails", "$orderDateDetails");
            projection.append("_id", 0);

            FindIterable<Document> rs = col.find(query).projection(projection);

            for (Document doc : rs) {

                if (doc == null) {
                    doc = new Document();
                }

                String rOrderId = CmmUtil.nvl(doc.getString("orderId"));
                String orderDate = CmmUtil.nvl(doc.getString("orderDate"));
                int itemSeq = doc.getInteger("itemSeq");
                String userId = CmmUtil.nvl(doc.getString("userId"));
                String sellerId = CmmUtil.nvl(doc.getString("sellerId"));
                String userAddress = CmmUtil.nvl(doc.getString("userAddress"));
                String userAddress2 = CmmUtil.nvl(doc.getString("userAddress2"));
                String orderDateDetails = CmmUtil.nvl(doc.getString("orderDateDetails"));


                orderDto.setOrderId(rOrderId);
                orderDto.setOrderDate(orderDate);
                orderDto.setItemSeq(itemSeq);
                orderDto.setSellerId(sellerId);
                orderDto.setUserId(userId);
                orderDto.setUserAddress(userAddress);
                orderDto.setUserAddress2(userAddress2);
                orderDto.setOrderDateDetails(orderDateDetails);

            }
        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info(code + "(HttpStatusCodeException) : " + statusCodeException);
            orderDto = new OrderDto();
        } catch (Exception e) {
            log.info("Exception : " + e);
            orderDto = new OrderDto();
        } finally {
            return orderDto;
        }
    }

    @Override
    public List<OrderDto> findOrderInfoByItemSeq(int itemSeq, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".findOrderInfoByItemSeq Start!");
        List<OrderDto> orderDtoList = new LinkedList<>();
        try {
            MongoCollection<Document> col = mongodb.getCollection(colNm);

            Document query = new Document();
            query.append("itemSeq", itemSeq);

            Document projection = new Document();
            projection.append("orderId", "$orderId");
            projection.append("orderDate", "$orderDate");
            projection.append("sellerId", "$sellerId");
            projection.append("orderDateDetails", "$orderDateDetails");
            projection.append("_id", 0);

            FindIterable<Document> rs = col.find(query).projection(projection);

            for (Document doc : rs) {
                if (doc == null) {
                    doc = new Document();
                }

                String orderId = CmmUtil.nvl(doc.getString("orderId"));
                String orderDate = CmmUtil.nvl(doc.getString("orderDate"));
                String sellerId = CmmUtil.nvl(doc.getString("sellerId"));
                String orderDateDetails = CmmUtil.nvl(doc.getString("orderDateDetails"));

                OrderDto orderDto = new OrderDto();

                orderDto.setOrderId(orderId);
                orderDto.setOrderDate(orderDate);
                orderDto.setSellerId(sellerId);
                orderDto.setOrderDateDetails(orderDateDetails);

                orderDtoList.add(orderDto);
            }
        } catch (HttpStatusCodeException statusCodeException) {
            int code = statusCodeException.getRawStatusCode();
            log.info(code + "(HttpStatusCodeException) : " + statusCodeException);
            orderDtoList = new LinkedList<>();
        } catch (Exception e) {
            log.info("Exception : " + e);
            orderDtoList = new LinkedList<>();
        } finally {
            return orderDtoList;
        }
    }
}

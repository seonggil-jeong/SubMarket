package com.submarket.userservice.service;

public interface ISubCheckService {
    boolean SubCheck(Integer subSeq) throws Exception;

    boolean checkHasSubByItemSeqAndUserId(Integer itemSeq, String userId) throws Exception;
}

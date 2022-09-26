package com.submarket.userservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SubRequest {

    private int itemSeq;

    private int subSeq;

    private int userSeq;
}

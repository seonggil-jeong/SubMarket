package com.submarket.userservice.dto;

import com.submarket.userservice.jpa.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeDto {
    private long likeSeq;
    private int itemSeq;

    private String userId;
}

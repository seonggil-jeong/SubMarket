package com.submarket.itemservice.dto;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    private int groupSeq;
    private String groupName;
    private List<ItemEntity> items;
}

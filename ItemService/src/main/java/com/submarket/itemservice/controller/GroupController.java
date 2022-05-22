package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.GroupDto;
import com.submarket.itemservice.service.impl.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;


    @GetMapping("/group/{groupSeq}")
    public ResponseEntity<GroupDto> findItemInfoByGroupSeq(@PathVariable int groupSeq) throws Exception {
        log.info(this.getClass().getName() + ".findItemInfoByGroupSeq Start!");

        GroupDto pDto = new GroupDto();
        pDto.setGroupSeq(groupSeq);

        GroupDto groupDto = groupService.findItemInfoByGroupSeq(pDto);

        if (groupDto.equals(null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        log.info(this.getClass().getName() + ".findItemInfoByGroupSeq End!");


        return ResponseEntity.ok().body(groupDto);

    }
}

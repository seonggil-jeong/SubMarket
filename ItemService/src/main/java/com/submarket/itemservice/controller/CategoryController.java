package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/category")
    public ResponseEntity<Map<String, Object>> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ">findAllCategory Start!");
        Map<String, Object> rMap = new HashMap<>();

        List<CategoryDto> categoryDtoList = categoryServiceImpl.findAllCategory();

        // Front  를 위해 Return Type 튜닝

        rMap.put("response", categoryDtoList);


        log.info(this.getClass().getName() + ">findAllCategory End!");

        return ResponseEntity.ok().body(rMap);
    }

    @GetMapping("/category/{categorySeq}")
    public ResponseEntity<CategoryDto> findItemInfoByCategorySeq(@PathVariable int categorySeq) throws Exception {
        log.info(this.getClass().getName() + ".findItemInfoByCategorySeq Start!");

        log.info("categorySeq : " + categorySeq);

        return ResponseEntity.ok().body(categoryServiceImpl.findCategory(
                CategoryDto.builder().categorySeq(categorySeq).build()));
    }
}

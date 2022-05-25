package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.service.impl.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<Map<String, Object>> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ">findAllCategory Start!");
        Map<String, Object> rMap = new HashMap<>();

        List<CategoryDto> categoryDtoList = categoryService.findAllCategory();

        // Front  를 위해 Return Type 튜닝
        Map<String, String> header = new HashMap<>();
        header.put("status", "1");
        header.put("resultCode", "200");

        Map<String, Object> body = new HashMap<>();
        body.put("categorys", categoryDtoList);
        Map<String, Object> entity = new HashMap<>();
        entity.put("header", header);
        entity.put("body", body);
        rMap.put("response", entity);


        log.info(this.getClass().getName() + ">findAllCategory End!");

        return ResponseEntity.ok().body(rMap);
    }

    @GetMapping("/category/{categorySeq}")
    public ResponseEntity<CategoryDto> findItemInfoByCategorySeq(@PathVariable int categorySeq) throws Exception {
        log.info(this.getClass().getName() + ".findItemInfoByCategorySeq Start!");

        log.info("categorySeq : " + categorySeq);
        CategoryDto pDto = new CategoryDto();
        pDto.setCategorySeq(categorySeq);

        CategoryDto categoryDto = categoryService.findCategory(pDto);

        log.info("categoryName : " + categoryDto.getCategoryName());

        log.info(this.getClass().getName() + ".findItemInfoByCategorySeq End!");
        return ResponseEntity.ok().body(categoryDto);
    }
}

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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<CategoryDto>> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ">findAllCategory Start!");
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        categoryDtoList = categoryService.findAllCategory();


        log.info(this.getClass().getName() + ">findAllCategory End!");

        return ResponseEntity.ok().body(categoryDtoList);
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

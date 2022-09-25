package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.service.CategoryService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Category API", description = "상품 Category 정보 API")
public class CategoryController {
    private final CategoryService categoryService;


    @Operation(summary = "모드 Category List 조회", description = "상품 CategoryList 조회", tags = {"category"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category List 조회 성공")
    })
    @GetMapping("/categories")
    @Timed(value = "category.findAll", longTask = true)
    public ResponseEntity<Map<String, Object>> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ">findAllCategory Start!");
        Map<String, Object> rMap = new HashMap<>();

        List<CategoryDto> categoryDtoList = categoryService.findAllCategory();

        // Front  를 위해 Return Type 튜닝

        rMap.put("response", categoryDtoList);


        log.info(this.getClass().getName() + ">findAllCategory End!");

        return ResponseEntity.ok().body(rMap);
    }


    @Operation(summary = "단일 Category 조회", description = "상품 Category 상세 조회", tags = {"category"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category List 조회 성공"),
            @ApiResponse(responseCode = "404", description = "Category 번호와 일치하는 정보를 찾을 수 없음")
    })
    @GetMapping("/categories/{categorySeq}")
    @Timed(value = "item.find.category", longTask = true)
    public ResponseEntity<CategoryDto> findItemInfoByCategorySeq(@PathVariable int categorySeq) throws Exception {
        log.info(this.getClass().getName() + ".findItemInfoByCategorySeq Start!");

        log.info("categorySeq : " + categorySeq);

        return ResponseEntity.ok().body(categoryService.findCategory(
                CategoryDto.builder().categorySeq(categorySeq).build()));
    }
}

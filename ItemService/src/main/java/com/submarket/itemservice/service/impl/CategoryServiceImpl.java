package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.exception.CategoryException;
import com.submarket.itemservice.exception.result.CategoryExceptionResult;
import com.submarket.itemservice.jpa.CategoryRepository;
import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.mapper.CategoryMapper;
import com.submarket.itemservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto findCategory(CategoryDto categoryDto) throws Exception {

        log.debug(this.getClass().getName() + ".findCategoryInfo : " + categoryDto.getCategorySeq());

        return CategoryMapper.INSTANCE.categoryEntityToCategoryDto(
                categoryRepository.findById(categoryDto.getCategorySeq())
                        .orElseThrow(() -> new CategoryException(CategoryExceptionResult.CATEGORY_NOT_FOUND)));
    }

    @Override
    public List<CategoryDto> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ".findAllCategory Start");

        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();



        return getCategoryDtoListFromCategoryEntityList(categoryEntityList);
    }

    // CategoryEntityList -> CategoryDtoList
    private List<CategoryDto> getCategoryDtoListFromCategoryEntityList(final List<CategoryEntity> categoryEntityList) {

        return categoryEntityList.stream().map(categoryEntity ->
                CategoryMapper.INSTANCE.categoryEntityToCategoryDto(categoryEntity)).collect(Collectors.toList());

    }
}

package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.CategoryDto;
import com.submarket.itemservice.jpa.CategoryRepository;
import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.mapper.CategoryMapper;
import com.submarket.itemservice.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service("CategoryService")
@Slf4j
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public CategoryDto findCategory(CategoryDto categoryDto) throws Exception {
        log.info(this.getClass().getName() + ".findCategory Start");
        int categorySeq = categoryDto.getCategorySeq();

        Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categorySeq);

        CategoryDto rDto = CategoryMapper.INSTANCE.categoryEntityToCategoryDto(categoryEntityOptional.get());

        log.info(this.getClass().getName() + ".findCategory End");
        return rDto;
    }

    @Override
    @Transactional
    public List<CategoryDto> findAllCategory() throws Exception {
        log.info(this.getClass().getName() + ".findAllCategory Start");

        Iterable<CategoryEntity> categoryEntityList = categoryRepository.findAll();

        List<CategoryDto> categoryDtoList = new LinkedList<>();

        categoryEntityList.forEach(categoryEntity -> {
            categoryDtoList.add(CategoryMapper.INSTANCE.categoryEntityToCategoryDto(categoryEntity));
        });

        log.info(this.getClass().getName() + ".findAllCategory End");
        return categoryDtoList;
    }
}

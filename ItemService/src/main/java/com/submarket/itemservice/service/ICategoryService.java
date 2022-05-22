package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {

    CategoryDto findCategory(CategoryDto categoryDto) throws Exception;

    List<CategoryDto> findAllCategory() throws Exception;

}

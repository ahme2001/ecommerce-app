package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository,  ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(c -> modelMapper.map(c,CategoryDTO.class))
                .toList();
        return new CategoryResponse(categoryDTOS);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO category) {
        Category c = modelMapper.map(category,Category.class);
        Category savedOne = categoryRepository.findByCategoryName(c.getCategoryName()).orElse(null);
        if (savedOne != null) {
            throw new APIException("Category already exists");
        }
        Category savedCategory = categoryRepository.save(c);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (category != null) {
            CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);
            categoryRepository.deleteById(id);
            return categoryDTO;
        }else {
            throw new ResourceNotFoundException("Category not found");
        }
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long id) {
        Category cat = categoryRepository.findById(id).orElse(null);
        if (cat == null){
            throw new ResourceNotFoundException("Category not found");
        }

        Category cat1 = categoryRepository.findByCategoryName(category.getCategoryName()).orElse(null);
        if (cat1 != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already found");
        }

        cat.setCategoryName(category.getCategoryName());
        Category c = categoryRepository.save(cat);
        return modelMapper.map(c,CategoryDTO.class);
    }
}

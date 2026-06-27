package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    
    private final ModelMapper modelMapper;


    public CategoryServiceImpl(CategoryRepository categoryRepository,  ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, sortBy)
                : Sort.by(Sort.Direction.DESC, sortBy);
        List<Category> categories;
        if (pageNumber != null && pageSize != null) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Category> categoryPage = categoryRepository.findAll(pageable);
            categories = categoryPage.getContent();
            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(c -> modelMapper.map(c,CategoryDTO.class))
                    .toList();
            return getCategoryResponse(categoryDTOS, categoryPage);
        }else {
            categories = categoryRepository.findAll();
            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(c -> modelMapper.map(c,CategoryDTO.class))
                    .toList();
            return getCategoryResponse(categoryDTOS, null);
        }
    }

    private @NonNull CategoryResponse getCategoryResponse(List<CategoryDTO> categoryDTOS, Page<Category> categoryPage) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        if (categoryPage != null) {
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setLastPage(categoryPage.isLast());
        }
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO category) {
        Category savedOne = categoryRepository.findByCategoryName(category.getCategoryName()).orElse(null);
        if (savedOne != null) {
            throw new APIException("Category already exists");
        }
        Category c = modelMapper.map(category,Category.class);
        c.setCategoryId(null);
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

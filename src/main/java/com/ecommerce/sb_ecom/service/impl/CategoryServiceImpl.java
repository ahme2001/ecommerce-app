package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.APIException;
import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public String deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return "Category has been deleted";
        }else {
            throw new ResourceNotFoundException("Category not found");
        }
    }

    @Override
    public String updateCategory(Category category, Long id) {
        Category cat = categoryRepository.findById(id).orElse(null);
        if (cat == null){
            throw new ResourceNotFoundException("Category not found");
        }

        Category cat1 = categoryRepository.findByCategoryName(category.getCategoryName()).orElse(null);
        if (cat1 != null) {
            throw new APIException("Category with name " + category.getCategoryName() + " already found");
        }

        cat.setCategoryName(category.getCategoryName());
        categoryRepository.save(cat);
        return "Category updated successfully";
    }
}

package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {


    private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        if (category.getCategoryId() == null) {
            category.setCategoryId(categories.size() + 1L);
        }
        categories.add(category);
    }

    public String deleteCategory(Long id) {
        boolean flag =  categories.removeIf(category -> category.getCategoryId().equals(id));
        if (flag)
            return "Category deleted successfully";
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
    }

    @Override
    public String updateCategory(Category category, Long id) {
        Category cat = categories.stream().filter(c -> c.getCategoryId().equals(id)).findFirst().orElse(null);

        if (cat == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }

        cat.setCategoryName(category.getCategoryName());
        return "Category updated successfully";
    }
}

package com.ecommerce.sb_ecom.service;


import com.ecommerce.sb_ecom.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(Long id);

    // update category
    // we need category Id , and also new data
    // we return ok if updated and fasle if not found
    String updateCategory(Category category, Long id);
}

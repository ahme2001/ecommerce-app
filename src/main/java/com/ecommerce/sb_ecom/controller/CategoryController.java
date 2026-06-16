package com.ecommerce.sb_ecom.controller;


import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    final private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }


    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable long id) {
        String status = categoryService.deleteCategory(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


    @PutMapping("/public/categories/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable long id,@Valid @RequestBody CategoryDTO category) {
        return new ResponseEntity<>(categoryService.updateCategory(category,id),HttpStatus.OK);
    }
}

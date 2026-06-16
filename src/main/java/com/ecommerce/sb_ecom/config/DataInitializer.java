package com.ecommerce.sb_ecom.config;


import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final CategoryService categoryService;

    public DataInitializer(CategoryService categoryRepository) {
        this.categoryService = categoryRepository;
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            CategoryDTO categoryDTO1 = new CategoryDTO( 1L, "Sports & fiiting");
            CategoryDTO categoryDTO2 = new CategoryDTO(2L, "Travel");
            CategoryDTO categoryDTO3 = new CategoryDTO(3L, "Toys");
            CategoryDTO categoryDTO4 = new CategoryDTO(4L, "Technology");
            CategoryDTO categoryDTO5 = new CategoryDTO(5L, "Clothing");
            CategoryDTO categoryDTO6 = new CategoryDTO(6L, "Learning tools");
            CategoryDTO categoryDTO7 = new CategoryDTO(7L, "Computer Science");
            CategoryDTO categoryDTO8 = new CategoryDTO(8L, "temps 1");
            CategoryDTO categoryDTO9 = new CategoryDTO(9L, "temps 2");
            CategoryDTO categoryDTO10 = new CategoryDTO(10L , "temps 3");
            CategoryDTO categoryDTO11 = new CategoryDTO(11L , "temps 4");

            categoryService.createCategory(categoryDTO1);
            categoryService.createCategory(categoryDTO2);
            categoryService.createCategory(categoryDTO3);
            categoryService.createCategory(categoryDTO4);
            categoryService.createCategory(categoryDTO5);
            categoryService.createCategory(categoryDTO6);
            categoryService.createCategory(categoryDTO7);
            categoryService.createCategory(categoryDTO8);
            categoryService.createCategory(categoryDTO9);
            categoryService.createCategory(categoryDTO10);
            categoryService.createCategory(categoryDTO11);
        };
    }
}

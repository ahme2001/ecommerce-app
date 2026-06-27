package com.ecommerce.sb_ecom.config;


import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.repository.RoleRepository;
import com.ecommerce.sb_ecom.repository.UserRepository;
import com.ecommerce.sb_ecom.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(CategoryService categoryRepository,  UserRepository userRepository,
                           RoleRepository roleRepository,  PasswordEncoder passwordEncoder) {
        this.categoryService = categoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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

            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUsername("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }
}

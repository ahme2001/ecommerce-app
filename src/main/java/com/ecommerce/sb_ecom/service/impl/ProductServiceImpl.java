package com.ecommerce.sb_ecom.service.impl;

import com.ecommerce.sb_ecom.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repository.CategoryRepository;
import com.ecommerce.sb_ecom.repository.ProductRepository;
import com.ecommerce.sb_ecom.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with id " + categoryId + " not found")
        );

        Product product = modelMapper.map(productDTO, Product.class);

        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        product.setProductId(null);
        productRepository.save(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc")
                ?  Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<Product>  productList = productPage.getContent();
        List<ProductDTO> productDTOS = productList.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("Category with id " + categoryId + " not found"));

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc")
                ?  Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        List<Product>  products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }


    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc")
                ?  Sort.Direction.ASC : Sort.Direction.DESC,sortBy);

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPage = productRepository.findByDescriptionContainingIgnoreCase(keyword, pageable);
        List<Product>  products = productPage.getContent();
        List<ProductDTO> productDTOS = products.stream().map(
                product -> modelMapper.map(product, ProductDTO.class)
        ).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    // assumption : all product data must be sent with request even if changed or not
    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        Product productFromDB = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product with id " + id + " not found")
        );

        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setSpecialPrice(product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice());

        productFromDB = productRepository.save(productFromDB);

        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long id) {
        Product productFromDB = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product with id " + id + " not found")
        );

        ProductDTO productDTO = modelMapper.map(productFromDB, ProductDTO.class);
        productRepository.delete(productFromDB);
        return productDTO;
    }


}

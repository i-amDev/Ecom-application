package com.project.ecom_application.service;

import com.project.ecom_application.dto.ProductRequest;
import com.project.ecom_application.dto.ProductResponse;
import com.project.ecom_application.entity.Product;
import com.project.ecom_application.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        mapToProduct(product, productRequest);
        Product savedEntity = productRepository.save(product);
        return mapToProductResponse(savedEntity);
    }

    private void mapToProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
    }

    private ProductResponse mapToProductResponse(Product savedEntity) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(savedEntity.getName());
        productResponse.setId(savedEntity.getId());
        productResponse.setDescription(savedEntity.getDescription());
        productResponse.setPrice(savedEntity.getPrice());
        productResponse.setCategory(savedEntity.getCategory());
        productResponse.setImageUrl(savedEntity.getImageUrl());
        productResponse.setStockQuantity(savedEntity.getStockQuantity());
        productResponse.setActive(savedEntity.getActive());

        return productResponse;
    }
}

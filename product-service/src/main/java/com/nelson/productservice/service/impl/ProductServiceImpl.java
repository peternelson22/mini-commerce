package com.nelson.productservice.service.impl;

import com.nelson.productservice.dto.ProductRequest;
import com.nelson.productservice.dto.ProductResponse;
import com.nelson.productservice.entity.Product;
import com.nelson.productservice.exception.ResourceNotFoundException;
import com.nelson.productservice.repository.ProductRepository;
import com.nelson.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public void addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        productRepository.save(product);
        log.info("Product Created");
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("Getting product for {}", id);
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("product with id (" + id + ") not found", "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long id, long quantity) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Product not found", "PRODUCT_NOT_FOUND"));
        if (product.getQuantity() < quantity){
            throw new ResourceNotFoundException("Product does not have the given quantity", "INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product quantity updated");
    }
}

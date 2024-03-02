package com.nelson.productservice.service;

import com.nelson.productservice.dto.ProductRequest;
import com.nelson.productservice.dto.ProductResponse;

public interface ProductService {
    void addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);

    void reduceQuantity(long id, long quantity);
}

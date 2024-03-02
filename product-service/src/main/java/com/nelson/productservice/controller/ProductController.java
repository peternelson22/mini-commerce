package com.nelson.productservice.controller;

import com.nelson.productservice.dto.ProductRequest;
import com.nelson.productservice.dto.ProductResponse;
import com.nelson.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest){
        productService.addProduct(productRequest);
        return new ResponseEntity<>("Successfully Created", CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable long id){
        return new ResponseEntity<>(productService.getProductById(id), OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable long id, @RequestParam long quantity){
        productService.reduceQuantity(id, quantity);
        return new ResponseEntity<>(OK);
    }
}

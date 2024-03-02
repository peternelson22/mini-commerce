package org.nelson.orderservice.external.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.nelson.orderservice.exception.OrderApiException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PRODUCT-SERVICE/api/product")
public interface ProductService {
    @PutMapping("/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable long id, @RequestParam long quantity);

    default void fallback(){
        throw new OrderApiException("Product service unavailable", "SERVICE_UNAVAILABLE", 500);
    }
}

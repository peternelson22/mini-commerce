package org.nelson.orderservice.controller;

import lombok.AllArgsConstructor;
import org.nelson.orderservice.dto.OrderRequest;
import org.nelson.orderservice.dto.OrderResponse;
import org.nelson.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@AllArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderRequest orderRequest){
        long orderId = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId){
        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        return  new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }
}

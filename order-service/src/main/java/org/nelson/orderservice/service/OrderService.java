package org.nelson.orderservice.service;

import org.nelson.orderservice.dto.OrderRequest;
import org.nelson.orderservice.dto.OrderResponse;

public interface OrderService {
    long createOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}

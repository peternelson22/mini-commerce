package org.nelson.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.nelson.orderservice.dto.OrderRequest;
import org.nelson.orderservice.dto.OrderResponse;
import org.nelson.orderservice.dto.ProductResponse;
import org.nelson.orderservice.entity.Order;
import org.nelson.orderservice.exception.OrderApiException;
import org.nelson.orderservice.external.client.PaymentService;
import org.nelson.orderservice.external.client.ProductService;
import org.nelson.orderservice.external.request.PaymentRequest;
import org.nelson.orderservice.external.response.PaymentResponse;
import org.nelson.orderservice.repository.OrderRepository;
import org.nelson.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long createOrder(OrderRequest orderRequest) {
        log.info("Placing order {}:", orderRequest.getProductId());

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order.builder()
                .amount(orderRequest.getAmount())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId())
                .build();
        orderRepository.save(order);
        log.info("Order placed successfully");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully");
            orderStatus = "PLACED";
        }catch (Exception e){
            log.error("Error occurred when trying to make payments");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Getting order detail for {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new  OrderApiException("Order not found for orderId " + orderId, "NOT_FOUND", 404 ));

        log.info("Invoking product service to get product with id {}", order.getProductId());

       ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/api/product/" + order.getProductId(),
                ProductResponse.class);

       log.info("Getting payment info from Payment Service");

       PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/api/payment/" + order.getId(),
                PaymentResponse.class);


       OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .status(paymentResponse.getStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();


        OrderResponse.ProductDetails  productDetails = null;
        if (productResponse != null) {
            productDetails = OrderResponse.ProductDetails.builder()
                   .name(productResponse.getName())
                   .quantity(productResponse.getQuantity())
                   .price(productResponse.getPrice())
                   .id(productResponse.getId())
                   .build();
        }

        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}

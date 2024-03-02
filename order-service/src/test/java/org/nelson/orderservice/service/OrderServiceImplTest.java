package org.nelson.orderservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nelson.orderservice.dto.OrderResponse;
import org.nelson.orderservice.dto.PaymentMode;
import org.nelson.orderservice.dto.ProductResponse;
import org.nelson.orderservice.entity.Order;
import org.nelson.orderservice.exception.OrderApiException;
import org.nelson.orderservice.external.client.PaymentService;
import org.nelson.orderservice.external.client.ProductService;
import org.nelson.orderservice.external.response.PaymentResponse;
import org.nelson.orderservice.repository.OrderRepository;
import org.nelson.orderservice.service.impl.OrderServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @DisplayName("Get Order Success Scenario")
    @Test
    void test_When_Order_Is_Successfully(){
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject("http://PRODUCT-SERVICE/api/product/" + order.getProductId(),
                ProductResponse.class))
                .thenReturn(getMockProductResponse());

        when(restTemplate.getForObject("http://PAYMENT-SERVICE/api/payment/" + order.getId(),
                PaymentResponse.class))
                .thenReturn(getMockPaymentResponse());

        OrderResponse orderResponse = orderService.getOrderDetails(1);

        verify(orderRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject("http://PRODUCT-SERVICE/api/product/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject("http://PAYMENT-SERVICE/api/payment/" + order.getProductId(),
                PaymentResponse.class);

        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());
    }

    @DisplayName("Get Order Failure Scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Found(){

        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        OrderApiException exception = assertThrows(OrderApiException.class,
                () -> orderService.getOrderDetails(1));
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(orderRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Is_Successfull(){

    }

    private PaymentResponse getMockPaymentResponse() {
       return PaymentResponse.builder()
                .paymentId(1)
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .status("ACCEPTED")
                .orderId(1)
                .paymentDate(Instant.now())
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .id(1)
                .name("CoCO")
                .price(3000)
                .quantity(300)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .id(1)
                .orderStatus("PLACED")
                .productId(1)
                .orderDate(Instant.now())
                .quantity(100)
                .amount(299)
                .build();
    }
}
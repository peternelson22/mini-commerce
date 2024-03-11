package org.nelson.orderservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nelson.orderservice.dto.OrderRequest;
import org.nelson.orderservice.dto.OrderResponse;
import org.nelson.orderservice.dto.PaymentMode;
import org.nelson.orderservice.dto.ProductResponse;
import org.nelson.orderservice.entity.Order;
import org.nelson.orderservice.exception.OrderApiException;
import org.nelson.orderservice.external.client.PaymentService;
import org.nelson.orderservice.external.client.ProductService;
import org.nelson.orderservice.external.request.PaymentRequest;
import org.nelson.orderservice.external.response.PaymentResponse;
import org.nelson.orderservice.repository.OrderRepository;
import org.nelson.orderservice.service.impl.OrderServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @DisplayName("Place Order - Success Scenario")
//    @Test
//    void test_When_Place_Order_Is_Successful(){
//        Order order = getMockOrder();
//        OrderRequest orderRequest = getMockOrderRequest();
//
//        when(orderRepository.save(any(Order.class)))
//                .thenReturn(order);
//
//        when(productService.reduceQuantity(anyLong(), anyLong()))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
//
//        when(paymentService.doPayment(any(PaymentRequest.class)))
//                .thenReturn(new ResponseEntity<Long>(1L,HttpStatus.OK));
//
//        long orderId = orderService.createOrder(orderRequest);
//
//        verify(orderRepository, times(2)).save(any());
//        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
//        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
//
//        assertEquals(order.getId(), orderId);
//
//    }
//
//    @DisplayName("Place Order - Payment Failed Scenario")
//    @Test
//    void test_When_PlaceOrder_Payment_Fails_then_Order_Placed(){
//        Order order = getMockOrder();
//        OrderRequest orderRequest = getMockOrderRequest();
//
//        when(orderRepository.save(any(Order.class)))
//                .thenReturn(order);
//
//        when(productService.reduceQuantity(anyLong(), anyLong()))
//                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
//
//        when(paymentService.doPayment(any(PaymentRequest.class)))
//                .thenThrow(new RuntimeException());
//        long orderId = orderService.createOrder(orderRequest);
//
//        verify(orderRepository, times(2)).save(any());
//        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
//        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
//
//        assertEquals(order.getId(), orderId);
//    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .quantity(12)
                .paymentMode(PaymentMode.CASH)
                .amount(122)
                .orderDate(Instant.now())
                .orderStatus("PLACED")
                .build();
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
package org.nelson.orderservice.exception;

import org.nelson.orderservice.external.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class OrderApiExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderApiException.class)
    public ResponseEntity<ErrorResponse> handleException(OrderApiException exception, WebRequest request){

        ErrorResponse message = new ErrorResponse(exception.getMessage(), exception.getErrorCode());
            return ResponseEntity.status(exception.getStatus()).body(message);
    }
}

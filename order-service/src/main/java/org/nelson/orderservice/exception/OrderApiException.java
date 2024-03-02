package org.nelson.orderservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderApiException extends RuntimeException{
    private String errorCode;
    private int status;

    public OrderApiException(String message, String errorCode, int status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}

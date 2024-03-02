package com.nelson.productservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private HttpStatus status;

    private String message;

    private LocalDateTime timestamp;
}

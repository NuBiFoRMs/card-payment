package com.nubiform.payment.config;

import com.nubiform.payment.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity paymentException(HttpServletRequest request, HttpServletResponse response, PaymentException ex) {
        log.error("code: {} message: {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.ok(ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity noHandlerFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message : {}", ex.getMessage());
        return paymentException(request, response, new PaymentException(ErrorCode.OperationNotSupported));
    }
}

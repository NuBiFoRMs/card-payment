package com.nubiform.payment.config;

import com.nubiform.payment.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, DataIntegrityViolationException.class})
    public ResponseEntity objectOptimisticLockingFailureExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return paymentException(request, response, new PaymentException(ErrorCode.PaymentIsProcessing));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity noHandlerFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.OperationNotSupported));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("exceptionClass: {}", ex.getClass().toString());
        log.error("exceptionMessage: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.UnknownError));
    }
}

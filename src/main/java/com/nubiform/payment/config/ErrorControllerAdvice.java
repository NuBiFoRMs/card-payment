package com.nubiform.payment.config;

import com.nubiform.payment.vo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<ErrorResponse> paymentException(HttpServletRequest request, HttpServletResponse response, PaymentException ex) {
        log.error("code: {} message: {}", ex.getCode(), ex.getMessage());
        return ResponseEntity.ok(ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler({ObjectOptimisticLockingFailureException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> objectOptimisticLockingFailureExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return paymentException(request, response, new PaymentException(ErrorCode.PaymentIsProcessing));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerFoundExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("message: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        log.error("exceptionClass: {}", ex.getClass().toString());
        log.error("exceptionMessage: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}

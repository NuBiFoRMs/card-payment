package com.nubiform.payment.controller;

import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.validator.SubmitValidator;
import com.nubiform.payment.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentController.API_V1_PAYMENT_URI)
public class PaymentController {

    public static final String API_V1_PAYMENT_URI = "/api/v1/payment";
    
    private final SubmitValidator submitValidator;

    private final PaymentService paymentService;

    private final ModelMapper modelMapper;

    @InitBinder("submitRequest")
    public void submitInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(submitValidator);
    }

    @PostMapping
    public ResponseEntity postPayment(@Valid @RequestBody SubmitRequest submitRequest, BindingResult bindingResult) throws Exception {
        log.debug("postPayment: {}", submitRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.ok(modelMapper.map(paymentService.submit(submitRequest), Response.class));
    }

    @DeleteMapping
    public ResponseEntity delPayment(@Valid @RequestBody CancelRequest cancelRequest, BindingResult bindingResult) throws Exception {
        log.debug("delPayment: {}", cancelRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.ok(modelMapper.map(paymentService.cancel(cancelRequest), Response.class));
    }

    @GetMapping
    public ResponseEntity getPayment(@Valid PaymentRequest paymentRequest, BindingResult bindingResult) throws Exception {
        log.debug("getPayment: {}", paymentRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(HttpStatus.BAD_REQUEST));
        }
        return ResponseEntity.ok(paymentService.payment(paymentRequest));
    }
}

package com.nubiform.payment.controller;

import com.nubiform.payment.config.ValidationException;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.validator.SubmitValidator;
import com.nubiform.payment.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    public static final String API_V1_PAYMENT_URI = "/api/v1/payments";

    private final SubmitValidator submitValidator;

    private final PaymentService paymentService;

    private final ModelMapper modelMapper;

    @InitBinder("submitRequest")
    public void submitInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(submitValidator);
    }

    @PostMapping
    public ResponseEntity<PaymentResponse<PaymentPayload>> postPayment(@Valid @RequestBody SubmitRequest submitRequest, BindingResult bindingResult) throws Exception {
        log.debug("postPayment: {}", submitRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            throw new ValidationException();
        }
        return ResponseEntity.ok(paymentService.submit(submitRequest));
    }

    @DeleteMapping
    public ResponseEntity<PaymentResponse<PaymentPayload>> delPayment(@Valid @RequestBody CancelRequest cancelRequest, BindingResult bindingResult) throws Exception {
        log.debug("delPayment: {}", cancelRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            throw new ValidationException();
        }
        return ResponseEntity.ok(paymentService.cancel(cancelRequest));
    }

    @GetMapping
    public ResponseEntity<PaymentResponse<Payment>> getPayment(@Valid PaymentRequest paymentRequest, BindingResult bindingResult) throws Exception {
        log.debug("getPayment: {}", paymentRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            throw new ValidationException();
        }
        return ResponseEntity.ok(paymentService.payment(paymentRequest));
    }
}

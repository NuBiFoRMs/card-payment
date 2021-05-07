package com.nubiform.payment.controller;

import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.validator.CancelValidator;
import com.nubiform.payment.validator.PaymentValidator;
import com.nubiform.payment.vo.CancelRequest;
import com.nubiform.payment.vo.PaymentRequest;
import com.nubiform.payment.vo.Response;
import com.nubiform.payment.vo.SubmitRequest;
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
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentValidator paymentValidator;
    private final CancelValidator cancelValidator;

    private final PaymentService paymentService;

    private final ModelMapper modelMapper;

    @InitBinder("submitRequest")
    public void submitInitBinder(WebDataBinder webDataBinder) {

    }

    @InitBinder("paymentRequest")
    public void paymentInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(paymentValidator);
    }

    @InitBinder("cancelRequest")
    public void cancelInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(cancelValidator);
    }

    @PostMapping
    public ResponseEntity<Response> postPayment(@Valid @RequestBody SubmitRequest submitRequest, BindingResult bindingResult) throws Exception {
        log.debug("postPayment: {}", submitRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(modelMapper.map(paymentService.submit(submitRequest), Response.class));
    }

    @DeleteMapping
    public ResponseEntity delPayment(@Valid @RequestBody CancelRequest cancelRequest, BindingResult bindingResult) {
        log.debug("delPayment: {}", cancelRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Response());
    }

    @GetMapping
    public ResponseEntity getPayment(@Valid @RequestBody PaymentRequest paymentRequest, BindingResult bindingResult) throws Exception {
        log.debug("getPayment: {}", paymentRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(paymentService.payment(paymentRequest));
    }
}

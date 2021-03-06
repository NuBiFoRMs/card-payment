package com.nubiform.payment.controller;

import com.nubiform.payment.config.ValidationException;
import com.nubiform.payment.service.PaymentService;
import com.nubiform.payment.validator.SubmitValidator;
import com.nubiform.payment.vo.*;
import com.nubiform.payment.vo.id.PaymentId;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public static final String PATH_VARIABLE_ID = "/{id}";
    public static final String API_V1_PAYMENT_URI_WITH_ID = API_V1_PAYMENT_URI + PATH_VARIABLE_ID;

    private final SubmitValidator submitValidator;

    private final PaymentService paymentService;

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

    @DeleteMapping(PATH_VARIABLE_ID)
    public ResponseEntity<PaymentResponse<PaymentPayload>> delPayment(@Parameter(schema = @Schema(type = "string")) @PathVariable("id") PaymentId paymentId,
                                                                      @Valid @RequestBody CancelRequest cancelRequest, BindingResult bindingResult) throws Exception {
        log.debug("delPayment: {} {}", paymentId, cancelRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            throw new ValidationException();
        }
        return ResponseEntity.ok(paymentService.cancel(paymentId, cancelRequest));
    }

    @GetMapping(PATH_VARIABLE_ID)
    public ResponseEntity<PaymentResponse<Payment>> getPayment(@Parameter(schema = @Schema(type = "string")) @PathVariable("id") PaymentId paymentId) throws Exception {
        log.debug("getPayment: {}", paymentId);
        return ResponseEntity.ok(paymentService.payment(paymentId));
    }
}

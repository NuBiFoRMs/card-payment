package com.nubiform.payment.api;

import com.nubiform.payment.api.vo.CancelRequest;
import com.nubiform.payment.api.vo.PaymentRequest;
import com.nubiform.payment.api.vo.Response;
import com.nubiform.payment.api.vo.SubmitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @InitBinder("submitRequest")
    public void initBinder(WebDataBinder webDataBinder) {

    }

    @PostMapping
    public ResponseEntity<Response> postPayment(@Valid @RequestBody SubmitRequest submitRequest, BindingResult bindingResult) {
        log.debug("postPayment: {}", submitRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Response());
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
    public ResponseEntity getPayment(@Valid @RequestBody PaymentRequest paymentRequest, BindingResult bindingResult) {
        log.debug("getPayment: {}", paymentRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Response());
    }
}

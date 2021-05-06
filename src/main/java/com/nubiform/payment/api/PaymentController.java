package com.nubiform.payment.api;

import com.nubiform.payment.api.vo.SubmitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @PostMapping
    public ResponseEntity postPayment(@Valid @RequestBody SubmitRequest submitRequest, BindingResult bindingResult) {
        log.debug("postPayment: {}", submitRequest);
        if (bindingResult.hasErrors()) {
            log.debug("bindingResult: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity delPayment() {
        log.debug("delPayment");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getPayment() {
        log.debug("getPayment");
        return ResponseEntity.ok().build();
    }
}

package com.nubiform.payment.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @PostMapping
    public ResponseEntity postPayment() {
        log.debug("postPayment");
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

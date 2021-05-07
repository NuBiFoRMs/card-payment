package com.nubiform.payment.vo;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class PaymentRequest {

    @Pattern(regexp = "^N[0-9]{19}$")
    private String id;

    public Long getLongId() {
        return Long.valueOf(id.replace("N", ""));
    }
}

package com.nubiform.payment.api.vo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ToString
public class SubmitRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,16}$")
    private String card;

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$")
    private String expiration;

    @NotBlank
    @Pattern(regexp = "^[0-9]{3}$")
    private String cvc;

    @Range(min = 0, max = 12)
    private int installment;

    @Range(min = 100, max = 1000000000)
    private long amount;

    @Range(min = 0, max = 1000000000)
    private long vat;
}

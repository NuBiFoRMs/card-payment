package com.nubiform.payment.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SubmitRequest {

    public static final double VAT_RATE = 11D;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,16}$")
    private String card;

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}$")
    private String expiration;

    @NotBlank
    @Pattern(regexp = "^[0-9]{3}$")
    private String cvc;

    @NotNull
    @Range(min = 0, max = 12)
    private Integer installment;

    @NotNull
    @Range(min = 100, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;

    public Long getVat() {
        if (this.vat == null) this.vat = Math.round(this.amount / VAT_RATE);
        return vat;
    }
}

package com.nubiform.payment.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SubmitRequest {

    public static final String CARD_REGEXP = "^[0-9]{10,16}$";
    public static final String EXPIRATION_REGEXP = "^[0-9]{4}$";
    public static final String CVC_REGEXP = "^[0-9]{3}$";

    @NotBlank
    @Pattern(regexp = CARD_REGEXP)
    private String card;

    @NotBlank
    @Pattern(regexp = EXPIRATION_REGEXP)
    private String expiration;

    @NotBlank
    @Pattern(regexp = CVC_REGEXP)
    private String cvc;

    @NotNull
    @Range(min = 0, max = 12)
    private Integer installment;

    @NotNull
    @Range(min = 100, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;

    public Integer getInstallment() {
        if (this.installment == 1) this.installment = 0;
        return this.installment;
    }
}

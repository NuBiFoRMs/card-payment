package com.nubiform.payment.api.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class CancelRequest {

    public static final double VAT_RATE = 11D;

    @Length(min = 20, max = 20)
    private String id;

    @NotNull
    @Range(min = 0, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;

    public Long getVat() {
        if (this.vat == null) this.vat = Math.round(this.amount / VAT_RATE);
        return vat;
    }
}

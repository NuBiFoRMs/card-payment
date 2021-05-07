package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CancelRequest {

    public static final String PRE_FIX = "N";
    public static final double VAT_RATE = 11D;

    @Pattern(regexp = "^" + PRE_FIX + "[0-9]{19}$")
    private String id;

    @NotNull
    @Range(min = 0, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;

    public void setId(String id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    @JsonIgnore
    public Long getLongId() {
        return Long.valueOf(id.replace(PRE_FIX, ""));
    }

    public Long getVat() {
        if (this.vat == null) this.vat = Math.round(this.amount / VAT_RATE);
        return vat;
    }
}

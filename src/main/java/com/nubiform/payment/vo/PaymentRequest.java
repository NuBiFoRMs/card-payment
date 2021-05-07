package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Pattern;

@Data
public class PaymentRequest {

    public static final String PRE_FIX = "N";

    @Pattern(regexp = "^" + PRE_FIX + "[0-9]{19}$")
    private String id;

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
}

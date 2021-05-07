package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PaymentResponse {

    public static final String PRE_FIX = "N";

    private String id;

    private String type;

    private String card;

    private String expiration;

    private String cvc;

    private Long amount;

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
}

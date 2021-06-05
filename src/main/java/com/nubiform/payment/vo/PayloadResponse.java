package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

@Data
public class PayloadResponse {

    private Long id;
    
    private PaymentPayload data;

    @JsonGetter("id")
    public String getId() {
        return Id.convert(this.id);
    }
}

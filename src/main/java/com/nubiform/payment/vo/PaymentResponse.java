package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

@Data
public class PaymentResponse<T> {

    private Long id;

    private T data;

    @JsonGetter("id")
    public String getId() {
        return Id.convert(this.id);
    }
}

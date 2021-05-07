package com.nubiform.payment.api.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PaymentRequest {

    @Length(min = 20, max = 20)
    private String id;
}

package com.nubiform.payment.api.vo;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
public class PaymentRequest {
    
    @Length(min = 20, max = 20)
    private String id;
}

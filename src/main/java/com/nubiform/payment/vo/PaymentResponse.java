package com.nubiform.payment.vo;

import com.nubiform.payment.vo.id.PaymentId;
import lombok.Data;

@Data
public class PaymentResponse<T> {

    private PaymentId id;

    private T data;
}

package com.nubiform.payment.vo;

import com.nubiform.payment.vo.id.PaymentId;

public class TestResponse {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public Long getLongId() {
        return PaymentId.convert(this.id);
    }

    public String getStringId() {
        return this.id;
    }
}
package com.nubiform.payment.vo;

public class TestResponse {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public Long getLongId() {
        return Id.convert(this.id);
    }

    public String getStringId() {
        return this.id;
    }
}
package com.nubiform.payment.vo;

public class TestResponse {

    public static final String PRE_FIX = "N";

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public Long getLongId() {
        return Long.valueOf(id.replace(PRE_FIX, ""));
    }

    public String getStringId() {
        return id;
    }
}
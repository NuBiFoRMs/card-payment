package com.nubiform.payment.vo.payload;

public interface PayloadSerializable {
    
    default String serialize() {
        return PayloadSerializer.serializer(this);
    }
}

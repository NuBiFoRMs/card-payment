package com.nubiform.payment.vo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PaymentIdDeserializer extends JsonDeserializer<PaymentId> {
    @Override
    public PaymentId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return PaymentId.of(p.getText());
    }
}

package com.nubiform.payment.config;

import com.nubiform.payment.vo.id.PaymentId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class PaymentIdFormatter implements Formatter<PaymentId> {
    @Override
    public PaymentId parse(String text, Locale locale) throws ParseException {
        log.debug("PaymentIdFormatter parse: {}", text);
        return PaymentId.of(text);
    }

    @Override
    public String print(PaymentId object, Locale locale) {
        log.debug("PaymentIdFormatter print: {}", object.toString());
        return object.toString();
    }
}

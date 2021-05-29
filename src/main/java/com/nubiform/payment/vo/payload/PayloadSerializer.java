package com.nubiform.payment.vo.payload;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.Optional;

public class PayloadSerializer {

    private static final int PAYLOAD_SIZE = 450;

    public static String serializer(Object object) {
        StringBuilder payloadBuilder = Optional
                .of(new StringBuilder())
                .map(value -> {
                    value.setLength(PAYLOAD_SIZE);
                    return value;
                })
                .get();

        Arrays.stream(FieldUtils.getAllFields(object.getClass()))
                .forEach(field -> {
                    Optional.ofNullable(field.getAnnotation(PayloadField.class))
                            .ifPresent(payloadField -> {
                                try {
                                    int start = payloadField.start();
                                    int end = payloadField.start() + payloadField.length();
                                    payloadBuilder.replace(start, end, payloadField
                                            .formatter()
                                            .format(
                                                    FieldUtils.readField(field, object, true),
                                                    payloadField.length()
                                            ));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                });

        return payloadBuilder.toString();
    }
}

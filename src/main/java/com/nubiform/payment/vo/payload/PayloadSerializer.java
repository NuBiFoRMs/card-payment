package com.nubiform.payment.vo.payload;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class PayloadSerializer {

    private static final int DEFAULT_PAYLOAD_SIZE = 500;

    public static String serializer(Object object) {
        StringBuilder payloadBuilder = new StringBuilder(DEFAULT_PAYLOAD_SIZE);

        Arrays.stream(FieldUtils.getAllFields(object.getClass()))
                .filter(field -> Optional.ofNullable(field.getAnnotation(PayloadField.class)).isPresent())
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(PayloadField.class).order()))
                .forEach(field -> {
                    Optional.ofNullable(field.getAnnotation(PayloadField.class))
                            .ifPresent(payloadField -> {
                                try {
                                    payloadBuilder.append(payloadField
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

        return String.join("", PayloadFormatter.NUMBER.format(payloadBuilder.length(), 4), payloadBuilder);
    }
}

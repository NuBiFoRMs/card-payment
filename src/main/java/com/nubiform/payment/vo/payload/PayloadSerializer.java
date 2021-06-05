package com.nubiform.payment.vo.payload;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class PayloadSerializer extends JsonSerializer<PayloadSerializable> {

    public static final int LENGTH_SIZE = 4;
    private static final int DEFAULT_PAYLOAD_SIZE = 500;

    @Override
    public void serialize(PayloadSerializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(serialize(value));
    }

    public static String serialize(PayloadSerializable payloadObject) {
        StringBuilder payloadBuilder = new StringBuilder(DEFAULT_PAYLOAD_SIZE);

        Arrays.stream(FieldUtils.getAllFields(payloadObject.getClass()))
                .filter(field -> Objects.nonNull(field.getAnnotation(PayloadField.class)))
                .sorted((f1, f2) -> {
                    int compare = Integer.compare(f1.getAnnotation(PayloadField.class).order(), f2.getAnnotation(PayloadField.class).order());
                    if (compare == 0)
                        return f1.getName().compareTo(f2.getName());
                    else return compare;
                })
                .forEach(field -> {
                    Optional.ofNullable(field.getAnnotation(PayloadField.class))
                            .ifPresent(payloadField -> {
                                try {
                                    payloadBuilder.append(payloadField
                                            .formatter()
                                            .format(
                                                    FieldUtils.readField(field, payloadObject, true),
                                                    payloadField.length()
                                            ));
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                });

        return StringUtils.join(PayloadFormatter.NUMBER.format(payloadBuilder.length(), LENGTH_SIZE), payloadBuilder);
    }
}

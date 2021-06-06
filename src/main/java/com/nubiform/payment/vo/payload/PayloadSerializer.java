package com.nubiform.payment.vo.payload;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

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
                .filter(PayloadSerializer::isPayloadField)
                .sorted(Comparator.comparing(PayloadSerializer::getOrder).thenComparing(Field::getName))
                .forEach(field -> {
                    PayloadField payloadField = field.getAnnotation(PayloadField.class);
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

        return StringUtils.join(PayloadFormatter.NUMBER.format(payloadBuilder.length(), LENGTH_SIZE), payloadBuilder);
    }


    private static int getOrder(Field field) {
        return field.getAnnotation(PayloadField.class).order();
    }

    private static boolean isPayloadField(Field field) {
        return Objects.nonNull(field.getAnnotation(PayloadField.class));
    }
}

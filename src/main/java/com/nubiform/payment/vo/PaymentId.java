package com.nubiform.payment.vo;

import com.nubiform.payment.config.ValidationException;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class PaymentId {

    public static final String PRE_FIX = "N";
    public static final String REGEXP = "^" + PRE_FIX + "[0-9]{19}$";

    private final long id;

    public PaymentId(Long id) {
        this.id = id;
    }

    public static PaymentId of(long id) {
        return new PaymentId(id);
    }

    public static PaymentId of(String id) {
        return new PaymentId(convert(id));
    }

    public static String convert(Long id) {
        if (id == null || id < 0) return StringUtils.leftPad("", 20, " ");
        return PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    public static Long convert(@NonNull String id) {
        if (!id.matches(REGEXP)) throw new ValidationException();
        return Long.valueOf(id.replace(PRE_FIX, ""));
    }

    public long value() {
        return this.id;
    }

    @Override
    public String toString() {
        return convert(this.id);
    }
}
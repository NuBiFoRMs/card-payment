package com.nubiform.payment.vo.id;

import com.nubiform.payment.config.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

public class PaymentId {

    public static final String PRE_FIX = "N";
    public static final String REGEXP = "^" + PRE_FIX + "[0-9]{19}$";

    private final long id;

    public PaymentId(long id) {
        this.id = id;
    }

    public static PaymentId of(long id) {
        if (id <= 0) throw new ValidationException();
        return new PaymentId(id);
    }

    public static PaymentId of(@NonNull String id) {
        return new PaymentId(convert(id));
    }

    public static String convert(long id) {
        if (id <= 0) throw new ValidationException();
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

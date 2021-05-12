package com.nubiform.payment.vo;

import org.apache.commons.lang3.StringUtils;

public class Id {

    public static final String PRE_FIX = "N";
    public static final String REGEXP = "^" + PRE_FIX + "[0-9]{19}$";

    public static String convert(Long id) {
        if (id == null || id < 0) return StringUtils.leftPad("", 20, " ");
        return PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    public static Long convert(String id) {
        if (id == null || !id.matches(REGEXP)) return null;
        return Long.valueOf(id.replace(PRE_FIX, ""));
    }
}

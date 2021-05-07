package com.nubiform.payment.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class SentData {

    public static final String PRE_FIX = "N";

    private String type;

    private Long id;

    private String card;

    private Integer installment;

    private String expiration;

    private String cvc;

    private Long amount;

    private Long vat;

    private Long originId;

    private String encryptedCard;

    private String getIdString(Long id) {
        if (id == null) return StringUtils.leftPad("", 20, " ");
        return PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder(500);
        data
                // 문자 10
                .append(StringUtils.rightPad(type, 10, " "))
                // 문자 20
                .append(getIdString(id))
                // 문자 20
                .append(StringUtils.rightPad(card, 20, " "))
                // 숫자(0) 2
                .append(StringUtils.leftPad(String.valueOf(installment), 2, "0"))
                // 숫자(L) 4
                .append(StringUtils.rightPad(expiration, 4, " "))
                // 숫자(L) 3
                .append(StringUtils.rightPad(cvc, 3, " "))
                // 숫자 10
                .append(StringUtils.leftPad(String.valueOf(amount), 10, " "))
                // 숫자(0) 10
                .append(StringUtils.leftPad(String.valueOf(vat), 10, "0"))
                // 문자 20
                .append(getIdString(originId))
                // 문자 300
                .append(StringUtils.rightPad(encryptedCard, 300, " "))
                // 문자 47
                .append(StringUtils.rightPad("", 47, " "))
        ;
        return StringUtils.leftPad(String.valueOf(data.length()), 4, " ") + data.toString();
    }
}

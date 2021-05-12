package com.nubiform.payment.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Card {

    public static final String SPLITTER = "|";
    public static final String SPLITTER_REGEXP = "\\" + SPLITTER;

    private String card;

    private String expiration;

    private String cvc;

    public Card(String data) {
        String[] split = data.split(SPLITTER_REGEXP);
        this.card = split[0];
        this.expiration = split[1];
        this.cvc = split[2];
    }

    public String toData() {
        return card + SPLITTER + expiration + SPLITTER + cvc;
    }
}

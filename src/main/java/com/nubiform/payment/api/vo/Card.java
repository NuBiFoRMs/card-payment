package com.nubiform.payment.api.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Card {

    private String card;

    private String expiration;

    private String cvc;

    public Card(String data) {
        String[] split = data.split("\\|");
        this.card = split[0];
        this.expiration = split[1];
        this.cvc = split[2];
    }

    public String toData() {
        return card + "|" + expiration + "|" + cvc;
    }
}

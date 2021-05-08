package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class History {

    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String card;

    private Integer installment;

    private Long amount;

    private Long vat;

    private Long originId;
}

package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class History extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 30)
    private String type;

    @Column(length = 300)
    private String card;

    private Integer installment;

    private Long amount;

    private Long vat;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Balance balance;
}

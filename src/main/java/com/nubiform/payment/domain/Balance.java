package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Balance {

    @Id
    private Long id;

    private String status;

    private String card;

    private Long amount;

    private Long vat;

    @Version
    private Long version;
}

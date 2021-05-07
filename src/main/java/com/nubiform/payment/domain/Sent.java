package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Sent {

    @Id
    private Long id;

    private String data;
}

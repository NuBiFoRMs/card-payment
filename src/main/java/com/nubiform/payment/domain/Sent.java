package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Sent {

    @Id
    private Long id;

    @Column(length = 500)
    private String data;
}

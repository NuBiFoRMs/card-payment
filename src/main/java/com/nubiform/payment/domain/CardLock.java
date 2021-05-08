package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class CardLock {

    @Id
    private String card;

    private LocalDateTime processDateTime;

    @Version
    private Long version;
}

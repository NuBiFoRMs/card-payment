package com.nubiform.payment.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class CardLock {

    @Id
    @Column(length = 300)
    private String card;

    @Column(length = 50)
    private String lockId;

    private LocalDateTime lockIdGeneratedAt;

    @Version
    private Long version;

    public String generateLockId() {
        this.lockId = UUID.randomUUID().toString();
        this.lockIdGeneratedAt = LocalDateTime.now();
        return this.lockId;
    }
}

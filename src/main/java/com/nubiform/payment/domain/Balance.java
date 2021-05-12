package com.nubiform.payment.domain;

import com.nubiform.payment.config.PaymentType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class Balance extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(length = 30)
    private String status;

    @Column(length = 300)
    private String card;

    private Integer installment;

    private Long amount;

    private Long vat;

    private Long remainAmount;

    private Long remainVat;

    @Version
    private Long version;

    public boolean cancel(Long amount, Long vat) {
        if (this.remainAmount - amount < 0) return false;
        if (this.remainVat - vat < 0) return false;
        if ((this.remainAmount - amount) < (this.vat - vat)) return false;
        this.remainAmount -= amount;
        this.remainVat -= vat;
        if (this.remainAmount == 0 && this.vat == 0) this.status = PaymentType.CANCEL;
        return true;
    }

    public boolean isCanceled() {
        if (PaymentType.CANCEL.equals(this.status)) return true;
        return false;
    }
}

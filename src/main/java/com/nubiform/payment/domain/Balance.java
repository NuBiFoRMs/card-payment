package com.nubiform.payment.domain;

import com.nubiform.payment.config.PaymentType;
import lombok.*;

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

    private String status;

    private String card;

    private Integer installment;

    private Long amount;

    private Long vat;

    @Version
    private Long version;

    public boolean cancel(Long amount, Long vat) {
        if (this.amount - amount < 0) return false;
        if (this.vat - vat < 0) return false;
        if ((this.amount - amount) < (this.vat - vat)) return false;
        this.amount -= amount;
        this.vat -= vat;
        if (this.amount == 0 && this.vat == 0) this.status = PaymentType.CANCEL;
        return true;
    }

    public boolean isCanceled() {
        if (PaymentType.CANCEL.equals(this.status)) return true;
        return false;
    }
}

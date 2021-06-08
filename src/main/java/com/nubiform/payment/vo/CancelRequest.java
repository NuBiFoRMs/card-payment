package com.nubiform.payment.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CancelRequest {

    @NotNull
    @Range(min = 1, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;
}

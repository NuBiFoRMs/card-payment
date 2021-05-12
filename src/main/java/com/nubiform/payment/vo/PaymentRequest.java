package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentRequest {

    @Pattern(regexp = Id.REGEXP)
    private String id;

    @JsonIgnore
    public Long getLongId() {
        return Id.convert(id);
    }
}

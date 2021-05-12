package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CancelRequest {

    @Pattern(regexp = Id.REGEXP)
    private String id;

    @NotNull
    @Range(min = 0, max = 1000000000)
    private Long amount;

    @Range(min = 0, max = 1000000000)
    private Long vat;

    @JsonIgnore
    public Long getLongId() {
        return Id.convert(id);
    }
}

package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaymentRequest {

    public static final String PRE_FIX = "N";

    @Pattern(regexp = "^" + PRE_FIX + "[0-9]{19}$")
    private String id;

    @JsonSetter("id")
    public void setId(String id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    @JsonIgnore
    public Long getLongId() {
        return Long.valueOf(id.replace(PRE_FIX, ""));
    }
}

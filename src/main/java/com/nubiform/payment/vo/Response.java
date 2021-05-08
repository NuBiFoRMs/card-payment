package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class Response {

    public static final String PRE_FIX = "N";

    private Long id;

    private String data;

    @JsonSetter("id")
    public void setId(String id) {
        this.id = Long.valueOf(id.replace(PRE_FIX, ""));
    }

    @JsonGetter("id")
    public String getId() {
        return PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    @JsonIgnore
    public Long getLongId() {
        return this.id;
    }
}

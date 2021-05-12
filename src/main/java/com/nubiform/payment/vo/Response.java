package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

@Data
public class Response {

    private Long id;

    private String data;

    @JsonGetter("id")
    public String getId() {
        return Id.convert(this.id);
    }
}

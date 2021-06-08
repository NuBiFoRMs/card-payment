package com.nubiform.payment.validator;

import com.nubiform.payment.vo.Id;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PaymentIdValidator implements ConstraintValidator<PaymentId, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.debug("isValid");
        return value.matches(Id.REGEXP);
    }
}

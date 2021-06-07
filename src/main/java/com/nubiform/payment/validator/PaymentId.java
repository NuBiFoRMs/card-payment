package com.nubiform.payment.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = PaymentIdValidator.class)
@Documented
public @interface PaymentId {

    String message() default "{invalid.id}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


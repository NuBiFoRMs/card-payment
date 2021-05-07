package com.nubiform.payment.validator;

import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.vo.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PaymentValidator implements Validator {

    private final HistoryRepository historyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PaymentRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PaymentRequest paymentRequest = (PaymentRequest) target;
        if (!historyRepository.existsById(paymentRequest.getLongId())) {
            errors.rejectValue("id", "invalid.id", new Object[]{paymentRequest.getId()}, "invalid id");
        }
    }
}

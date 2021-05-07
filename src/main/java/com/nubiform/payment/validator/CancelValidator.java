package com.nubiform.payment.validator;

import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.vo.CancelRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class CancelValidator implements Validator {

    private final HistoryRepository historyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CancelRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CancelRequest cancelRequest = (CancelRequest) target;
        if (!historyRepository.existsById(cancelRequest.getLongId())) {
            errors.rejectValue("id", "invalid.id", new Object[]{cancelRequest.getId()}, "invalid id");
        }
    }
}

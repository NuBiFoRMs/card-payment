package com.nubiform.payment.validator;

import com.nubiform.payment.repository.HistoryRepository;
import com.nubiform.payment.vo.SubmitRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class SubmitValidator implements Validator {

    private final HistoryRepository historyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SubmitRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SubmitRequest submitRequest = (SubmitRequest) target;
        try {
            LocalDate expiration = LocalDate.of(
                    Integer.parseInt(String.valueOf(LocalDate.now().getYear()).substring(0, 2) + submitRequest.getExpiration().substring(2, 4)),
                    Integer.parseInt(submitRequest.getExpiration().substring(0, 2)),
                    1);
            log.debug("expiration: {}", expiration);
        } catch (Exception e) {
            errors.rejectValue("expiration", "invalid.expiration", new Object[]{submitRequest.getExpiration()}, "invalid expiration");
        }
    }
}

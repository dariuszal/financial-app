package io.klix.financing.service;

import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.validation.ValidationResult;
import io.klix.financing.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationValidationService implements Validator<ApplicationRequest> {
    private final RequestService requestService;

    @Override
    public ValidationResult validate(ApplicationRequest applicationRequest) {
        Boolean applicationExistsByEmail = requestService.hasPendingApplicationsByEmail(applicationRequest.getEmail());
        if (applicationExistsByEmail) {
            return ValidationResult.failure(String.format("Application for %s is still processing, please wait for the result, before submitting another application", applicationRequest.getEmail()));
        }
        return ValidationResult.success();
    }
}

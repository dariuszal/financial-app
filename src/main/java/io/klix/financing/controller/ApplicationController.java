package io.klix.financing.controller;

import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.http.response.ApplicationResponse;
import io.klix.financing.http.response.ApplicationStatusResponse;
import io.klix.financing.service.ApplicationValidationService;
import io.klix.financing.service.ApplicationService;
import io.klix.financing.util.ApplicationUtil;
import io.klix.financing.validation.ValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicationValidationService applicationValidationService;

    @PostMapping("/submit")
    public ResponseEntity<ApplicationResponse> submitApplication(@RequestBody @Valid ApplicationRequest applicationRequest) {
        ValidationResult validationResult = applicationValidationService.validate(applicationRequest);
        if (validationResult.isValid()) {
            applicationService.submitApplications(applicationRequest);
            return ok(ApplicationUtil.buildSuccessfullApplicationResponse(applicationRequest.getApplicationId()));
        }
        return ok(ApplicationUtil.buildResponseWithMessage(validationResult.getMessage()));
    }

    @GetMapping("/{applicationId}")
    public ApplicationStatusResponse getApplicationResponse(@PathVariable UUID applicationId) {
        return applicationService.getApplicationDetails(applicationId);
    }
}

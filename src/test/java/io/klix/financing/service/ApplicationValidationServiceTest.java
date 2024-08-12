package io.klix.financing.service;
import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ApplicationValidationServiceTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private ApplicationValidationService applicationValidationService;

    private ApplicationRequest applicationRequest;

    @BeforeEach
    void setUp() {
        applicationRequest = new ApplicationRequest();
        applicationRequest.setEmail("test@example.com");
    }

    @Test
    void testValidateWhenApplicationExists() {
        when(requestService.hasPendingApplicationsByEmail(applicationRequest.getEmail())).thenReturn(true);

        ValidationResult result = applicationValidationService.validate(applicationRequest);

        assertFalse(result.isValid()); // Expect failure
        assertEquals(
                "Application for test@example.com is still processing, please wait for the result, before submitting another application",
                result.getMessage()
        );
        verify(requestService, times(1)).hasPendingApplicationsByEmail(applicationRequest.getEmail());
    }

    @Test
    void testValidateWhenApplicationDoesNotExist() {
        when(requestService.hasPendingApplicationsByEmail(applicationRequest.getEmail())).thenReturn(false);

        ValidationResult result = applicationValidationService.validate(applicationRequest);

        assertTrue(result.isValid()); // Expect success
        assertNull(result.getMessage());
        verify(requestService, times(1)).hasPendingApplicationsByEmail(applicationRequest.getEmail());
    }

    @Test
    void testValidateWithNullEmail() {
        applicationRequest.setEmail(null);

        when(requestService.hasPendingApplicationsByEmail(null)).thenReturn(false);

        ValidationResult result = applicationValidationService.validate(applicationRequest);

        assertTrue(result.isValid());  // Assuming service does not consider null email as pending
        assertNull(result.getMessage());
        verify(requestService, times(1)).hasPendingApplicationsByEmail(null);
    }
}
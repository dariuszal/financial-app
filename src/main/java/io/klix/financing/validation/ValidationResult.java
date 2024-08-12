package io.klix.financing.validation;

import lombok.Getter;

@Getter
public class ValidationResult {
    private boolean valid;
    private String message;

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
}

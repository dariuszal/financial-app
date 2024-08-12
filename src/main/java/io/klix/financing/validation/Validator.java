package io.klix.financing.validation;

public interface Validator<T> {
    ValidationResult validate(T object);
}

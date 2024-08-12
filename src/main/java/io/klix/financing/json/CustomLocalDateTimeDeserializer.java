package io.klix.financing.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        try {
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                LocalDate date = LocalDate.parse(text, DateTimeFormatter.ISO_DATE);
                return date.atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new RuntimeException("Failed to parse date: " + text, ex);
            }
        }
    }
}

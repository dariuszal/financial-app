package io.klix.financing.http.request;

import io.klix.financing.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {

    private UUID applicationId = UUID.randomUUID();
    @Pattern(regexp = "\\+371[0-9]{8}")
    private String phone;
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Invalid email address"
    )
    @Schema(description = "User's email address", example = "user@example.com")
    private String email;
    @NotNull(message = "monthly income cannot be null")
    private Integer monthlyIncome;
    @NotNull(message = "monthlyExpenses cannot be null")
    private Integer monthlyExpenses;
    @NotNull(message = "maritalStatus cannot be null")
    private MaritalStatus maritalStatus;
    @NotNull(message = "agreeToBeScored cannot be null")
    private Boolean agreeToBeScored;
    @NotNull(message = "amount cannot be null")
    private BigDecimal amount;
    @NotNull(message = "dependents cannot be null")
    private Integer dependents;
    @NotNull(message = "agreeToDataSharing cannot be null")
    private Boolean agreeToDataSharing;
}

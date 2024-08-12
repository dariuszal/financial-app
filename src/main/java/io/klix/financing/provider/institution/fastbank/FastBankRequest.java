package io.klix.financing.provider.institution.fastbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.klix.financing.provider.AbstractProviderRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FastBankRequest extends AbstractProviderRequest {
    @JsonProperty("phoneNumber")
    private String phone;
    @JsonProperty("email")
    private String email;
    @JsonProperty("monthlyIncomeAmount")
    private Integer monthlyIncomeAmount;
    @JsonProperty("monthlyCreditLiabilities")
    private Integer monthlyCreditLiabilities;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("dependents")
    private Integer dependents;
    @JsonProperty("agreeToDataSharing")
    private Boolean agreeToDataSharing;
}

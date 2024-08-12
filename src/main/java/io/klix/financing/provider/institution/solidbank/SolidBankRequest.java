package io.klix.financing.provider.institution.solidbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.klix.financing.enums.MaritalStatus;
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
public class SolidBankRequest extends AbstractProviderRequest {
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("email")
    private String email;
    @JsonProperty("monthlyIncome")
    private Integer monthlyIncomeAmount;
    @JsonProperty("monthlyExpenses")
    private Integer monthlyCreditLiabilities;
    @JsonProperty("maritalStatus")
    private MaritalStatus maritalStatus;
    @JsonProperty("agreeToBeScored")
    private Boolean agreeToBeScored;
    @JsonProperty("amount")
    private BigDecimal amount;

}

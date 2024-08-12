package io.klix.financing.provider.institution.solidbank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.klix.financing.json.CustomLocalDateTimeDeserializer;
import io.klix.financing.provider.AbstractProviderOffer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SolidBankOffer extends AbstractProviderOffer {
    private Double monthlyPaymentAmount;
    private BigDecimal totalRepaymentAmount;
    private Integer numberOfPayments;
    private Double annualPercentageRate;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime firstRepaymentDate;
}

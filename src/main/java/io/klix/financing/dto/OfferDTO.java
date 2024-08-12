package io.klix.financing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OfferDTO {
    private Double monthlyPaymentAmount;
    private BigDecimal totalRepaymentAmount;
    private Integer numberOfPayments;
    private Double annualPercentageRate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime firstRepaymentDate;
}

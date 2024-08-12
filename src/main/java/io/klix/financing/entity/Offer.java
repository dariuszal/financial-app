package io.klix.financing.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "monthly_payment_amount")
    private Double monthlyPaymentAmount;
    @Column(name = "total_repayment_amount")
    private BigDecimal totalRepaymentAmount;
    @Column(name = "number_of_payments")
    private Integer numberOfPayments;
    @Column(name = "annual_percentage_rate")
    private Double annualPercentageRate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "first_repayment_date")
    private LocalDateTime firstRepaymentDate;
    @OneToOne(mappedBy = "offer")
    private Request request;
}

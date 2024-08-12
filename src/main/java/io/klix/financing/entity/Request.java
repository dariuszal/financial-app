package io.klix.financing.entity;

import io.klix.financing.enums.MaritalStatus;
import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "provider_request_id")
    private UUID providerRequestId;
    @Column(name = "application_id")
    private UUID applicationId;
    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "monthly_income")
    private Integer monthlyIncome;
    @Column(name = "monthly_liabilities")
    private Integer monthlyLiabilities;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "dependents")
    private Integer dependents;
    @Column(name = "agreeToDataSharing")
    private Boolean agreeToDataSharing;
    @Column(name = "martial_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @Column(name = "agree_to_be_scored")
    private Boolean agreeToBeScored;
    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @Column(name = "request_status_reason")
    private String requestStatusReason;
    @Column(name = "requested_at")
    @CreatedDate
    private LocalDateTime requestedAt;
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    private Offer offer;
    @Column(name = "request_finalized")
    private Boolean requestFinalized = false;
    @Column(name = "request_finalized_at")
    private LocalDateTime requestFinalizedAt;
}

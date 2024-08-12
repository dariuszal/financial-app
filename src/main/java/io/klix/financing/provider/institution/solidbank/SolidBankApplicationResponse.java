package io.klix.financing.provider.institution.solidbank;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.klix.financing.provider.AbstractProviderApplicationResponse;
import io.klix.financing.provider.institution.fastbank.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolidBankApplicationResponse extends AbstractProviderApplicationResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("status")
    private ApplicationStatus status;
    @JsonProperty("request")
    private SolidBankRequest solidBankRequest;
    @JsonProperty("offer")
    private SolidBankOffer solidBankOffer;
}

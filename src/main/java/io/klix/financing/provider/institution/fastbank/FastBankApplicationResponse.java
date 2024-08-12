package io.klix.financing.provider.institution.fastbank;

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
public class FastBankApplicationResponse extends AbstractProviderApplicationResponse {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("status")
    private ApplicationStatus status;
    @JsonProperty("request")
    private FastBankRequest fastBankRequest;
    @JsonProperty("offer")
    private FastBankOffer fastBankOffer;
}

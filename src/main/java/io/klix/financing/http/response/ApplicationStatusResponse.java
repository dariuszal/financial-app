package io.klix.financing.http.response;

import io.klix.financing.dto.OfferDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ApplicationStatusResponse {
    private UUID applicationId;
    private List<OfferDTO> offers;
}

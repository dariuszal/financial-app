package io.klix.financing.service;

import io.klix.financing.dto.OfferDTO;
import io.klix.financing.entity.Request;
import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.http.response.ApplicationStatusResponse;
import io.klix.financing.map.OfferMapper;
import io.klix.financing.provider.AbstractProvider;
import io.klix.financing.provider.ProviderFactory;
import io.klix.financing.provider.institution.InstitutionDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {
    private final ProviderFactory providerFactory;
    private final InstitutionDetailsService institutionDetailsService;
    private final RequestService requestService;

    public void submitApplications(ApplicationRequest applicationRequest) {
        List<AbstractProvider<?>> providerList = providerFactory.providerList();
        for (AbstractProvider<?> provider : providerList) {
            provider.submitApplications(institutionDetailsService.getProviderAccount(provider.getProvider()), applicationRequest);
        }
    }

    public ApplicationStatusResponse getApplicationDetails(UUID applicationId) {
        List<Request> requestsByApplicationId = requestService.getRequestsByApplicationId(applicationId);
        if (!requestsByApplicationId.isEmpty()) {
            List<OfferDTO> offerDTOS = new ArrayList<>();
            requestsByApplicationId.forEach(request -> {
                if (request.getOffer() != null) {
                    offerDTOS.add(OfferMapper.mapOfferToDTO(request.getOffer()));
                }
            });
            return ApplicationStatusResponse.builder().applicationId(applicationId).offers(offerDTOS).build();
        }
        return null;
    }

}

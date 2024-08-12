package io.klix.financing.service;

import io.klix.financing.provider.AbstractProvider;
import io.klix.financing.provider.ProviderFactory;
import io.klix.financing.provider.institution.InstitutionDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationStatusSyncService {
    private final ProviderFactory providerFactory;
    private final InstitutionDetailsService institutionDetailsService;
    private final OfferMailService offerMailService;

    @Scheduled(fixedRate = 20000) // Check every 20 seconds
    public void syncApplicationStatus() {
        List<AbstractProvider<?>> providerList = providerFactory.providerList();
        for (AbstractProvider<?> provider : providerList) {
            provider.syncApplicationStatus(institutionDetailsService.getProviderAccount(provider.getProvider()));
        }
        offerMailService.initiateOfferSending();
    }
}

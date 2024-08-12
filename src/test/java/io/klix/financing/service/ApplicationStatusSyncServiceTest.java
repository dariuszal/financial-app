package io.klix.financing.service;


import io.klix.financing.provider.AbstractProvider;
import io.klix.financing.provider.ProviderFactory;
import io.klix.financing.provider.institution.InstitutionDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationStatusSyncServiceTest {

    @Mock
    private ProviderFactory providerFactory;

    @Mock
    private InstitutionDetailsService institutionDetailsService;

    @Mock
    private OfferMailService offerMailService;

    @InjectMocks
    private ApplicationStatusSyncService applicationStatusSyncService;

    private List<AbstractProvider<?>> providerList;
    private AbstractProvider<?> provider;

    @BeforeEach
    void setUp() {
        provider = mock(AbstractProvider.class);
        providerList = new ArrayList<>();
        providerList.add(provider);
    }

    @Test
    void testSyncApplicationStatus() {
        when(providerFactory.providerList()).thenReturn(providerList);

        // Manually invoke the @Scheduled method
        ReflectionTestUtils.invokeMethod(applicationStatusSyncService, "syncApplicationStatus");

        // Verify that the providers were used to sync application status
        verify(providerFactory, times(1)).providerList();
        verify(provider, times(1)).syncApplicationStatus(any());
        verify(offerMailService, times(1)).initiateOfferSending();
    }

    @Test
    void testSyncApplicationStatusWithEmptyProviderList() {
        when(providerFactory.providerList()).thenReturn(new ArrayList<>());

        ReflectionTestUtils.invokeMethod(applicationStatusSyncService, "syncApplicationStatus");

        // Verify that the offer mail service is still called even with no providers
        verify(providerFactory, times(1)).providerList();
        verify(provider, never()).syncApplicationStatus(any());
        verify(offerMailService, times(1)).initiateOfferSending();
    }
}
package io.klix.financing.service;

import io.klix.financing.entity.Offer;
import io.klix.financing.entity.Request;
import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.http.response.ApplicationStatusResponse;
import io.klix.financing.provider.AbstractProvider;
import io.klix.financing.provider.ProviderFactory;
import io.klix.financing.provider.institution.InstitutionDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ProviderFactory providerFactory;

    @Mock
    private InstitutionDetailsService institutionDetailsService;

    @Mock
    private RequestService requestService;

    @InjectMocks
    private ApplicationService applicationService;

    @Mock
    private AbstractProvider<?> provider;

    private List<AbstractProvider<?>> providerList;
    private UUID applicationId;
    private ApplicationRequest applicationRequest;

    @BeforeEach
    void setUp() {
        providerList = new ArrayList<>();
        providerList.add(provider);
        applicationId = UUID.randomUUID();
        applicationRequest = new ApplicationRequest();
    }

    @Test
    void testSubmitApplicationsWithEmptyProviderList() {
        when(providerFactory.providerList()).thenReturn(new ArrayList<>());
        applicationService.submitApplications(applicationRequest);
        verify(providerFactory, times(1)).providerList();
        verify(provider, never()).submitApplications(any(), any());
    }

    @Test
    void testSubmitApplicationsWithMultipleProviders() {
        AbstractProvider<?> secondProvider = mock(AbstractProvider.class);
        providerList.add(secondProvider);

        when(providerFactory.providerList()).thenReturn(providerList);

        applicationService.submitApplications(applicationRequest);

        verify(providerFactory, times(1)).providerList();
        verify(provider, times(1)).submitApplications(any(), eq(applicationRequest));
        verify(secondProvider, times(1)).submitApplications(any(), eq(applicationRequest));
    }

    @Test
    void testGetApplicationDetailsWithNullApplicationId() {
        assertDoesNotThrow(() -> applicationService.getApplicationDetails(null));
    }

    @Test
    void testGetApplicationDetailsWhenRequestHasNullOffer() {
        List<Request> requests = new ArrayList<>();
        Request request = new Request();
        request.setOffer(null);
        requests.add(request);

        when(requestService.getRequestsByApplicationId(applicationId)).thenReturn(requests);

        ApplicationStatusResponse response = applicationService.getApplicationDetails(applicationId);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(0, response.getOffers().size());

        verify(requestService, times(1)).getRequestsByApplicationId(applicationId);
    }

    @Test
    void testSubmitApplications() {
        when(providerFactory.providerList()).thenReturn(providerList);
        applicationService.submitApplications(applicationRequest);
        verify(providerFactory, times(1)).providerList();
        verify(provider, times(1)).submitApplications(any(), eq(applicationRequest));
    }

    @Test
    void testGetApplicationDetailsWithOffers() {
        List<Request> requests = new ArrayList<>();
        Offer offer = new Offer();
        Request request = new Request();
        request.setOffer(offer);
        requests.add(request);

        when(requestService.getRequestsByApplicationId(applicationId)).thenReturn(requests);

        ApplicationStatusResponse response = applicationService.getApplicationDetails(applicationId);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(1, response.getOffers().size());

        verify(requestService, times(1)).getRequestsByApplicationId(applicationId);
    }

    @Test
    void testGetApplicationDetailsWhenAllRequestsHaveNullOffers() {
        List<Request> requests = new ArrayList<>();
        Request request1 = new Request();
        request1.setOffer(null);
        Request request2 = new Request();
        request2.setOffer(null);
        requests.add(request1);
        requests.add(request2);

        when(requestService.getRequestsByApplicationId(applicationId)).thenReturn(requests);

        ApplicationStatusResponse response = applicationService.getApplicationDetails(applicationId);

        assertNotNull(response);
        assertEquals(applicationId, response.getApplicationId());
        assertEquals(0, response.getOffers().size());

        verify(requestService, times(1)).getRequestsByApplicationId(applicationId);
    }
}
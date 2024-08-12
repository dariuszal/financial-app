package io.klix.financing.provider.institution.solidbank;

import io.klix.financing.entity.Request;
import io.klix.financing.enums.RequestStatus;
import io.klix.financing.map.ApplicationMapper;
import io.klix.financing.map.OfferMapper;
import io.klix.financing.provider.AbstractProvider;
import io.klix.financing.provider.AbstractProviderApplicationResponse;
import io.klix.financing.provider.account.ProviderAccount;
import io.klix.financing.provider.institution.fastbank.enums.ApplicationStatus;
import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

import static io.klix.financing.enums.ProviderEnum.SOLID_BANK;

@Service
@Slf4j
public class SolidBankAPIProvider extends AbstractProvider<SolidBankRequest> {
    private RestTemplate restTemplate;
    private RequestService requestService;

    @Value("${provider.SOLID_BANK.expire-after-hours}")
    private int requestExpireInHours;

    public SolidBankAPIProvider(
            RestTemplate restTemplate,
            RequestService requestService
    ) {
        super(SOLID_BANK);
        this.restTemplate = restTemplate;
        this.requestService = requestService;
    }

    @Override
    public void submitApplications(ProviderAccount providerAccount, ApplicationRequest applicationRequest) {
        Request request = requestService.saveInitialRequest(applicationRequest, provider);
        if (providerAccount.getEnabled()) {
            try {
                String url = UriComponentsBuilder.fromHttpUrl(providerAccount.getUrl()).toUriString();
                SolidBankRequest solidBankRequest = buildProviderRequest(applicationRequest);
                ResponseEntity<? extends AbstractProviderApplicationResponse> response =
                        restTemplate.postForEntity(url, solidBankRequest, SolidBankApplicationResponse.class);
                persistRequest(response, request);
            } catch (
                    Exception e) {
                log.error("Exception while submitting application request to {}", provider, e);
            }
        } else {
            handleProviderDisabled(request);
            requestService.saveRequest(request);
            log.info("{} provider is not enabled, therefore application will not be send to this provider.", provider);
        }
    }

    @Override
    protected String buildStatusSyncUrl(ProviderAccount providerAccount, Request request) {
        return UriComponentsBuilder.fromHttpUrl(providerAccount.getUrl())
                .path("/{requestId}")
                .buildAndExpand(request.getProviderRequestId().toString())
                .toUriString();
    }

    @Override
    protected void processStatusResponse(Request request, ResponseEntity<? extends AbstractProviderApplicationResponse> response) {
        SolidBankApplicationResponse responseBody = (SolidBankApplicationResponse) response.getBody();
        if (responseBody != null) {
            if (responseBody.getStatus() == ApplicationStatus.PROCESSED) {
                handleStatusProcessed(request, responseBody);
            } else if (responseBody.getStatus() == ApplicationStatus.DRAFT) {
                handleStatusDraft(request, requestExpireInHours);
            }
        } else {
            handleNoResponse(request);
        }
        requestService.saveRequest(request);
    }

    @Override
    protected SolidBankRequest buildProviderRequest(ApplicationRequest applicationRequest) {
        return SolidBankRequest.builder()
                .phone(applicationRequest.getPhone())
                .email(applicationRequest.getEmail())
                .monthlyIncomeAmount(applicationRequest.getMonthlyIncome())
                .monthlyCreditLiabilities(applicationRequest.getMonthlyExpenses())
                .maritalStatus(applicationRequest.getMaritalStatus())
                .agreeToBeScored(applicationRequest.getAgreeToBeScored())
                .amount(applicationRequest.getAmount()).build();
    }

    @Override
    protected void persistRequest(ResponseEntity<? extends AbstractProviderApplicationResponse> providerResponse, Request request) {
        try {
            SolidBankApplicationResponse response = (SolidBankApplicationResponse) providerResponse.getBody();
            ApplicationMapper.mapSolidBankApplicationResponseToRequest(response, request);
            requestService.saveRequest(request);
        } catch (Exception e) {
            log.warn("Exception while persisting {} with response {}", providerResponse, providerResponse, e);
        }
    }

    @Override
    public void syncApplicationStatus(ProviderAccount providerAccount) {
        try {
            List<Request> pendingRequests = requestService.getPendingRequestsByProvider(provider);
            if (!pendingRequests.isEmpty()) {
                log.info("Request sync started for provider: {}, pending requests: {}", provider, pendingRequests.size());
                pendingRequests.forEach(request -> syncSingleRequestStatus(providerAccount, request));
                log.info("Request sync ended for provider: {}", provider);
            } else {
                log.info("Provider: {}, does not have any pending requests", provider);
            }
        } catch (Exception e) {
            log.error("Exception while syncing application requests for {}", provider, e);
        }
    }

    private void syncSingleRequestStatus(ProviderAccount providerAccount, Request request) {
        try {
            String url = buildStatusSyncUrl(providerAccount, request);
            ResponseEntity<? extends AbstractProviderApplicationResponse> response = restTemplate.getForEntity(url, SolidBankApplicationResponse.class);
            processStatusResponse(request, response);
        } catch (Exception e) {
            log.error("Error syncing status for request ID: {}", request.getId(), e);
        }
    }


    @Override
    protected void handleStatusProcessed(Request request, AbstractProviderApplicationResponse responseBody) {
        if (responseBody == null) {
            handleNoResponse(request);
            return;
        }

        SolidBankApplicationResponse solidbankResponse = (SolidBankApplicationResponse) responseBody;
        if (solidbankResponse.getSolidBankOffer() != null) {
            request.setOffer(OfferMapper.mapSolidBankOfferAndRequestToOffer(solidbankResponse.getSolidBankOffer(), request));
            request.setRequestStatus(RequestStatus.COMPLETED);
        } else {
            request.setRequestStatus(RequestStatus.REJECTED);
        }
        request.setProcessedAt(LocalDateTime.now());
    }
}

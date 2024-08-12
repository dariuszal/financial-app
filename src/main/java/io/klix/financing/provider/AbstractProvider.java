package io.klix.financing.provider;

import io.klix.financing.entity.Request;
import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.enums.RequestStatus;
import io.klix.financing.provider.account.ProviderAccount;
import io.klix.financing.http.request.ApplicationRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Slf4j
public abstract class AbstractProvider<T extends AbstractProviderRequest> {
    protected ProviderEnum provider;

    public AbstractProvider(ProviderEnum provider) {
        this.provider = provider;
    }

    public abstract void submitApplications(
            ProviderAccount providerAccount,
            ApplicationRequest applicationRequest);

    protected abstract T buildProviderRequest(ApplicationRequest applicationRequest);

    protected abstract void persistRequest(ResponseEntity<? extends AbstractProviderApplicationResponse> response, Request request);

    public abstract void syncApplicationStatus(ProviderAccount providerAccount);

    protected abstract String buildStatusSyncUrl(ProviderAccount providerAccount, Request request);

    protected abstract void processStatusResponse(Request request, ResponseEntity<? extends AbstractProviderApplicationResponse> response);

    protected void finalizeRequest(Request request) {
        request.setRequestFinalized(true);
        request.setRequestFinalizedAt(LocalDateTime.now());
    }

    protected void handleNoResponse(Request request) {
        request.setRequestStatus(RequestStatus.NOT_FOUND);
        request.setRequestStatusReason("Provider has no data on this request");
        finalizeRequest(request);
    }

    protected void handleProviderDisabled(Request request) {
        request.setRequestStatus(RequestStatus.NOT_SUBMITTED);
        request.setRequestStatusReason("Provider is disabled");
        finalizeRequest(request);
    }

    protected void handleStatusDraft(Request request, int requestExpireInHours) {
        if (request.getRequestedAt().isBefore(LocalDateTime.now().minusHours(requestExpireInHours))) {
            log.info("Request: {} was still not processed by provider {} and will be marked as expired because it is older than {} hours", request.getId(), provider, requestExpireInHours);
            request.setRequestStatus(RequestStatus.EXPIRED);
            finalizeRequest(request);
        }
    }
    protected abstract void handleStatusProcessed(Request request, AbstractProviderApplicationResponse responseBody);

}
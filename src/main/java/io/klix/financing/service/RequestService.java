package io.klix.financing.service;

import io.klix.financing.entity.Request;
import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.enums.RequestStatus;
import io.klix.financing.http.request.ApplicationRequest;
import io.klix.financing.map.ApplicationMapper;
import io.klix.financing.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;

    public Request createRequest(Request request) {
        return requestRepository.save(request);
    }

    public List<Request> getPendingRequestsByProvider(ProviderEnum provider) {
        return requestRepository.findAllPendingRequestsByProvider(provider, RequestStatus.PROCESSING);
    }

    public List<Request> getRequestsForSending() {
        return requestRepository.findAllByRequestFinalizedIsFalseAndRequestStatus(RequestStatus.COMPLETED);
    }

    public Boolean hasPendingApplicationsByEmail(String email) {
        return requestRepository.existsByEmailAndRequestStatus(email, RequestStatus.PROCESSING);
    }

    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    public Request saveInitialRequest(ApplicationRequest applicationRequest, ProviderEnum provider) {
        Request request = ApplicationMapper.mapInitialApplicationToRequest(applicationRequest, provider);
        request.setRequestStatus(RequestStatus.PROCESSING);
        return createRequest(request);
    }

    public List<Request> getRequestsByApplicationId(UUID applicationId) {
        return requestRepository.findAllByApplicationId(applicationId);
    }

    public void finalizeRequest(Request r) {
        r.setRequestFinalized(true);
        r.setRequestFinalizedAt(LocalDateTime.now());
        saveRequest(r);
    }
}

package io.klix.financing.service;

import io.klix.financing.entity.Offer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferService {
    private final RequestService requestService;

    public void finalizeOffersRequests(List<Offer> offers) {
        offers.forEach(offer -> requestService.finalizeRequest(offer.getRequest()));
    }
}

package io.klix.financing.map;

import io.klix.financing.dto.OfferDTO;
import io.klix.financing.entity.Offer;
import io.klix.financing.entity.Request;
import io.klix.financing.provider.institution.fastbank.FastBankOffer;
import io.klix.financing.provider.institution.solidbank.SolidBankOffer;

public class OfferMapper {
    public static Offer mapFastBankOfferAndRequestToOffer(FastBankOffer fastBankOffer, Request request) {
        Offer offer = new Offer();
        offer.setRequest(request);
        offer.setFirstRepaymentDate(fastBankOffer.getFirstRepaymentDate());
        offer.setAnnualPercentageRate(fastBankOffer.getAnnualPercentageRate());
        offer.setTotalRepaymentAmount(fastBankOffer.getTotalRepaymentAmount());
        offer.setMonthlyPaymentAmount(fastBankOffer.getMonthlyPaymentAmount());
        offer.setNumberOfPayments(fastBankOffer.getNumberOfPayments());
        return offer;
    }

    public static Offer mapSolidBankOfferAndRequestToOffer(SolidBankOffer solidBankOffer, Request request) {
        Offer offer = new Offer();
        offer.setRequest(request);
        offer.setFirstRepaymentDate(solidBankOffer.getFirstRepaymentDate());
        offer.setAnnualPercentageRate(solidBankOffer.getAnnualPercentageRate());
        offer.setTotalRepaymentAmount(solidBankOffer.getTotalRepaymentAmount());
        offer.setMonthlyPaymentAmount(solidBankOffer.getMonthlyPaymentAmount());
        offer.setNumberOfPayments(solidBankOffer.getNumberOfPayments());
        return offer;
    }

    public static OfferDTO mapOfferToDTO(Offer offer) {
        return OfferDTO.builder()
                .firstRepaymentDate(offer.getFirstRepaymentDate())
                .monthlyPaymentAmount(offer.getMonthlyPaymentAmount())
                .numberOfPayments(offer.getNumberOfPayments())
                .annualPercentageRate(offer.getAnnualPercentageRate())
                .totalRepaymentAmount(offer.getTotalRepaymentAmount())
                .build();
    }
}

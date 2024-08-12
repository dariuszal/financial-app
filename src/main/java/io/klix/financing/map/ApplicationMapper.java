package io.klix.financing.map;

import io.klix.financing.entity.Request;
import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.provider.institution.fastbank.FastBankApplicationResponse;
import io.klix.financing.provider.institution.fastbank.FastBankRequest;
import io.klix.financing.provider.institution.solidbank.SolidBankApplicationResponse;
import io.klix.financing.provider.institution.solidbank.SolidBankRequest;
import io.klix.financing.http.request.ApplicationRequest;

public class ApplicationMapper {

    public static void mapFastBankApplicationResponseToRequest(FastBankApplicationResponse response, Request request) {
        FastBankRequest fastBankRequest = response.getFastBankRequest();
        request.setProviderRequestId(response.getId());
        request.setAmount(fastBankRequest.getAmount());
        request.setEmail(fastBankRequest.getEmail());
        request.setPhone(fastBankRequest.getPhone());
        request.setAgreeToDataSharing(fastBankRequest.getAgreeToDataSharing());
        request.setDependents(fastBankRequest.getDependents());
        request.setMonthlyLiabilities(fastBankRequest.getMonthlyCreditLiabilities());
    }
    public static void mapSolidBankApplicationResponseToRequest(SolidBankApplicationResponse response, Request request) {
        SolidBankRequest fastBankRequest = response.getSolidBankRequest();
        request.setProviderRequestId(response.getId());
        request.setAmount(fastBankRequest.getAmount());
        request.setEmail(fastBankRequest.getEmail());
        request.setPhone(fastBankRequest.getPhone());
        request.setAgreeToBeScored(fastBankRequest.getAgreeToBeScored());
        request.setMaritalStatus(fastBankRequest.getMaritalStatus());
        request.setMonthlyLiabilities(fastBankRequest.getMonthlyCreditLiabilities());
    }

    public static Request mapInitialApplicationToRequest(ApplicationRequest applicationRequest, ProviderEnum provider) {
        Request request = new Request();
        request.setProvider(provider);
        request.setApplicationId(applicationRequest.getApplicationId());
        request.setAmount(applicationRequest.getAmount());
        request.setEmail(applicationRequest.getEmail());
        request.setPhone(applicationRequest.getPhone());
        request.setAgreeToDataSharing(applicationRequest.getAgreeToDataSharing());
        request.setDependents(applicationRequest.getDependents());
        request.setMonthlyLiabilities(applicationRequest.getMonthlyExpenses());
        return request;
    }
}

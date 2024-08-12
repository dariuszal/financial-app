package io.klix.financing.service;

import io.klix.financing.entity.Offer;
import io.klix.financing.entity.Request;
import io.klix.financing.util.ApplicationUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferMailService {
    private final JavaMailSender javaMailSender;
    private final OfferService offerService;
    private final RequestService requestService;

    @Transactional
    public void initiateOfferSending() {
        List<Request> nonFinalizedRequests = requestService.getRequestsForSending();
        if (!nonFinalizedRequests.isEmpty()) {
            Map<UUID, List<Request>> requestsByApplicationId = getRequestsByApplicationId(nonFinalizedRequests);
            requestsByApplicationId.forEach((applicationId, requests) -> {
                if (!requests.isEmpty()) {
                    boolean allOffersReadyPerApplication = true;
                    String email = requests.get(0).getEmail();
                    List<Offer> offers = new ArrayList<>();
                    for (Request r : requests) {
                        if (r.getOffer() != null) {
                            offers.add(r.getOffer());
                        } else {
                            allOffersReadyPerApplication = false;
                        }
                    }
                    if (!offers.isEmpty() && allOffersReadyPerApplication) {
                        try {
                            sendOffersEmail(email, offers, applicationId);
                            offerService.finalizeOffersRequests(offers);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } else {
            log.info("All requests are finalized at the moment.");
        }
    }

    private Map<UUID, List<Request>> getRequestsByApplicationId(List<Request> nonFinalizedRequests) {
        return nonFinalizedRequests.stream()
                .collect(Collectors.groupingBy(
                        Request::getApplicationId
                ));
    }


    public void sendOffersEmail(String to, List<Offer> offers, UUID applicationId) throws MessagingException {
        log.info("Sending offers to: {}", to);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Loan Offers");
        helper.setText(buildEmailContent(offers, applicationId), true);

        javaMailSender.send(message);
    }

    public String buildEmailContent(List<Offer> offers, UUID applicationId) {
        StringBuilder content = new StringBuilder();
        content.append("<h1>Your Loan Offers for application " + applicationId + "</h1>");
        content.append("<ul>");
        for (Offer offer : offers) {
            content.append("<li>")
                    .append("Monthly Payment: ").append(offer.getMonthlyPaymentAmount()).append("<br>")
                    .append("Total Repayment: ").append(offer.getTotalRepaymentAmount()).append("<br>")
                    .append("Number of Payments: ").append(offer.getNumberOfPayments()).append("<br>")
                    .append("Annual Percentage Rate: ").append(offer.getAnnualPercentageRate()).append("<br>")
                    .append("First Repayment Date: ").append(offer.getFirstRepaymentDate()).append("<br>")
                    .append("</li>");
        }
        content.append("</ul>");
        content.append("<a href=\"").append(ApplicationUtil.getApplicationStatusUrl(applicationId.toString())).append("\">View Offer Details</a>").append("<br>");
        return content.toString();
    }

}

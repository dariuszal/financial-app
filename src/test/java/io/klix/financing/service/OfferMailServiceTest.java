package io.klix.financing.service;

import io.klix.financing.entity.Offer;
import io.klix.financing.entity.Request;
import io.klix.financing.util.ApplicationUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferMailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private OfferService offerService;

    @Mock
    private RequestService requestService;

    @InjectMocks
    private OfferMailService offerMailService;

    private List<Request> nonFinalizedRequests;
    private List<Offer> offers;
    private UUID applicationId;
    private Request request;
    private Offer offer;

    @BeforeEach
    void setUp() {
        applicationId = UUID.randomUUID();

        offer = new Offer();
        offer.setMonthlyPaymentAmount(500.0);
        offer.setTotalRepaymentAmount(BigDecimal.valueOf(6000.0));
        offer.setNumberOfPayments(12);
        offer.setAnnualPercentageRate(5.5);
        offer.setFirstRepaymentDate(LocalDateTime.parse("2024-09-01T00:00:00"));

        request = new Request();
        request.setEmail("test@example.com");
        request.setApplicationId(applicationId);
        request.setOffer(offer);

        nonFinalizedRequests = new ArrayList<>();
        nonFinalizedRequests.add(request);

        offers = new ArrayList<>();
        offers.add(offer);
    }

    @Test
    void testInitiateOfferSendingWithNonFinalizedRequests() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        when(requestService.getRequestsForSending()).thenReturn(nonFinalizedRequests);

        offerMailService.initiateOfferSending();

        verify(requestService, times(1)).getRequestsForSending();
        verify(javaMailSender, times(1)).send(mimeMessage);
        verify(offerService, times(1)).finalizeOffersRequests(offers);
    }

    @Test
    void testInitiateOfferSendingWithNoNonFinalizedRequests() {
        when(requestService.getRequestsForSending()).thenReturn(new ArrayList<>());

        offerMailService.initiateOfferSending();

        verify(requestService, times(1)).getRequestsForSending();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
        verify(offerService, never()).finalizeOffersRequests(anyList());
    }


    @Test
    void testSendOffersEmail() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        offerMailService.sendOffersEmail(request.getEmail(), offers, applicationId);

        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testBuildEmailContent() {
        String emailContent = offerMailService.buildEmailContent(offers, applicationId);

        assertTrue(emailContent.contains("<h1>Your Loan Offers for application " + applicationId + "</h1>"));
        assertTrue(emailContent.contains("Monthly Payment: 500.0"));
        assertTrue(emailContent.contains("Total Repayment: 6000.0"));
        assertTrue(emailContent.contains("Number of Payments: 12"));
        assertTrue(emailContent.contains("Annual Percentage Rate: 5.5"));
        assertTrue(emailContent.contains("First Repayment Date: 2024-09-01"));
        assertTrue(emailContent.contains("<a href=\"" + ApplicationUtil.getApplicationStatusUrl(applicationId.toString()) + "\">View Offer Details</a>"));
    }
}
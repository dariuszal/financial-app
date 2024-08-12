package io.klix.financing.util;

import io.klix.financing.http.response.ApplicationResponse;

import java.util.UUID;

public class ApplicationUtil {
    public static ApplicationResponse buildSuccessfullApplicationResponse(UUID applicationId) {
        return ApplicationResponse.builder()
                .message("Thank you for your application. You will receive offers to your email as soon as they are ready. You can also see application status using the link.")
                .url(getApplicationStatusUrl(applicationId.toString()))
                .build();
    }

    public static ApplicationResponse buildResponseWithMessage(String msg) {
        return ApplicationResponse.builder()
                .message(msg)
                .build();
    }

    public static String getApplicationStatusUrl(String applicationId) {
        return "http://localhost:8080/api/application/" + applicationId;
    }
}

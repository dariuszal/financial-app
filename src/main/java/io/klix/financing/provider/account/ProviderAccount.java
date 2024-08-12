package io.klix.financing.provider.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProviderAccount {
    private String key; //if need key to access api for the future;
    private String url;
    private Boolean enabled;
}

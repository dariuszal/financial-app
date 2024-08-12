package io.klix.financing.provider.institution;

import io.klix.financing.enums.ProviderEnum;
import io.klix.financing.provider.account.ProviderAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstitutionDetailsService {
    private final Environment environment;

    public ProviderAccount getProviderAccount(ProviderEnum provider) {
        try {
            return ProviderAccount.builder()
                    .url(environment.getProperty("provider." + provider.toString() + ".url"))
                    .enabled(Boolean.valueOf(environment.getProperty("provider." + provider + ".enabled")))
                    .build();
        } catch (Exception e) {
            log.error("Could not retrieve provider :{} account", provider.toString());
            return null;
        }
    }
}

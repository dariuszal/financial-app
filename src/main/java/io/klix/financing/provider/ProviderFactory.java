package io.klix.financing.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProviderFactory {
    private final List<AbstractProvider<?>> providers;

    public List<AbstractProvider<?>> providerList() {
        return Collections.unmodifiableList(providers);
    }
}

package io.klix.financing.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class BeansConfig {
    @Bean
    public RestTemplate restTemplate() {
        return (new RestTemplateBuilder())
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofSeconds(30))
                .messageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()))
                .build();
    }
}

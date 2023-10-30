package com.wanted.socialMediaIntegratedFeed.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@TestConfiguration
public class TestConfig {

    @Bean
    public TestRestTemplate testRestTemplate(RestTemplateBuilder builder) {

        HttpClient httpClient = HttpClientBuilder.create().build();
        return new TestRestTemplate(builder.requestFactory(()
                -> new HttpComponentsClientHttpRequestFactory(httpClient)));
    }

}

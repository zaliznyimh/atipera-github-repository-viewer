package com.zaliznyimh.github_viewer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .baseUrl(githubBaseUrl)
                .defaultHeader("User-Agent", "GitHub-Repositories-Viewer")
                .defaultHeader("Accept", "application/vnd.github.v3+json");
    }

    @Bean
    public RestClient restClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder.build();
    }
}

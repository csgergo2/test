package com.example.jobprocessor.service;

import com.example.jobprocessor.model.AuthTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AuthServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);

    private final RestClient restClient;
    private final String authUrl;
    private final AtomicReference<TokenHolder> tokenHolder = new AtomicReference<>();

    public AuthServiceClient(RestClient.Builder builder,
                             @Value("${auth.service.url:http://auth.example.com}") String authUrl) {
        this.restClient = builder.baseUrl(authUrl).build();
        this.authUrl = authUrl;
    }

    public synchronized String getToken() {
        TokenHolder holder = tokenHolder.get();
        if (holder == null || holder.expiration.isBefore(Instant.now().plusSeconds(5))) {
            holder = fetchToken();
            tokenHolder.set(holder);
        }
        return holder.token;
    }

    private TokenHolder fetchToken() {
        log.info("Fetching new auth token from {}", authUrl);
        AuthTokenResponse response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(AuthTokenResponse.class);
        long expiresIn = response != null ? response.getExpiresIn() : 0;
        String token = response != null ? response.getAccessToken() : null;
        return new TokenHolder(token, Instant.now().plusSeconds(expiresIn));
    }

    private static class TokenHolder {
        private final String token;
        private final Instant expiration;

        private TokenHolder(String token, Instant expiration) {
            this.token = token;
            this.expiration = expiration;
        }
    }
}


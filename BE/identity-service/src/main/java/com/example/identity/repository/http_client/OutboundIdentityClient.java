package com.example.identity.repository.http_client;

import com.example.identity.dto.request.ExchangeTokenRequest;
import com.example.identity.dto.response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "outbound-identity", url = "${spring.security.oauth2.client.google.token.base-url}")
public interface OutboundIdentityClient {
    @PostMapping(value = "${spring.security.oauth2.client.google.token.path}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}

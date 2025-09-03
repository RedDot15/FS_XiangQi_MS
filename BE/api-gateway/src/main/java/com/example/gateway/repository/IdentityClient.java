package com.example.gateway.repository;

import com.example.gateway.helper.ResponseObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/tokens/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseObject> introspect(@RequestHeader("Authorization") String token);
}

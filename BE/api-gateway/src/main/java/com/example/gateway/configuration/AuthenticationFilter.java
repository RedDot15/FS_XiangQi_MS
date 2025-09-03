package com.example.gateway.configuration;

import com.example.gateway.helper.ResponseObject;
import com.example.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    private static final String[] PUBLIC_POST_ENDPOINTS = {
            "/identity/auth/tokens",
            "/identity/auth/outbound/.*/authenticate",
            "/identity/auth/tokens/refresh",
            "/identity/auth/tokens/introspect",
            "/identity/users/social",
            "/identity/users"
    };

    private static final String[] PUBLIC_GET_ENDPOINTS = {
    };

    @Value("${app.api-prefix}")
    @NonFinal
    String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        // Get token from authorization header
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader))
            return buildUnauthenticatedResponse(exchange.getResponse());

        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);

        return identityService.introspect(token)
                .flatMap(responseObject -> chain.filter(exchange))
                .onErrorResume(throwable -> buildUnauthenticatedResponse(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        if (HttpMethod.POST.equals(request.getMethod())) {
            return Arrays.stream(PUBLIC_POST_ENDPOINTS)
                    .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
        } else if (HttpMethod.GET.equals(request.getMethod())) {
            return Arrays.stream(PUBLIC_GET_ENDPOINTS)
                    .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
        } else {
            return false;
        }
    }

    Mono<Void> buildUnauthenticatedResponse(ServerHttpResponse response){
        ResponseObject responseObject = new ResponseObject("failed", "Unauthenticated error.", null);

        String body = null;
        try {
            body = objectMapper.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}

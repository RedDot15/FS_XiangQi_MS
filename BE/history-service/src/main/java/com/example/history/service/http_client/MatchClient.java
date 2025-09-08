package com.example.history.service.http_client;

import com.example.history.config.security.AuthenticationRequestInterceptor;
import com.example.history.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "match-service", url = "${app.services.match}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface MatchClient {
    @GetMapping(value = "/internal/matches", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject getFinishedMatch(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String userId);
}

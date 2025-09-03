package com.example.match.repository.http_client;

import com.example.match.config.security.AuthenticationRequestInterceptor;
import com.example.match.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/profiles/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject getById(@PathVariable String userId);

    @PatchMapping(value = "/internal/profiles/{userId}/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject updateRating(@PathVariable String userId, @RequestBody Integer changedRating);
}

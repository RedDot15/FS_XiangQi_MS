package com.example.history.service.http_client;

import com.example.history.config.security.AuthenticationRequestInterceptor;
import com.example.history.helper.ResponseObject;
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
}

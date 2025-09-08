package com.example.queue.repository.http_client;

import com.example.queue.config.security.AuthenticationRequestInterceptor;
import com.example.queue.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/internal/profiles/{userId}/rating", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject getRatingById(@RequestBody String userId);
}

package com.example.onlineUser.repository;

import com.example.onlineUser.config.security.AuthenticationRequestInterceptor;
import com.example.onlineUser.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface ProfileClient {
    @PostMapping(value = "/profiles/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject getById(@PathVariable String userId);
}

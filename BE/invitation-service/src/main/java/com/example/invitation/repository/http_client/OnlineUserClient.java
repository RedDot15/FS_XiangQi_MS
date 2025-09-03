package com.example.invitation.repository.http_client;

import com.example.invitation.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "online-user-service", url = "${app.services.online-user}")
public interface OnlineUserClient {
    @GetMapping(value = "/online-users/{displayedName}")
    ResponseObject getByUsername(@PathVariable String displayedName);
}

package com.example.queue.repository.http_client;

import com.example.queue.config.security.AuthenticationRequestInterceptor;
import com.example.queue.dto.request.MatchContractRequest;
import com.example.queue.helper.ResponseObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "profile-service", url = "${app.services.match-contract}",
        configuration = { AuthenticationRequestInterceptor.class })
public interface MatchContractClient {
    @PostMapping(value = "/internal/match-contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject create(@PathVariable MatchContractRequest request);
}

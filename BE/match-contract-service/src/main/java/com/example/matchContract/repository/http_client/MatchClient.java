package com.example.matchContract.repository.http_client;

import com.example.matchContract.dto.request.MatchRequest;
import com.example.matchContract.helper.ResponseObject;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "match-service", url = "${app.services.match}")
public interface MatchClient {
    @PostMapping(value = "/matchs", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseObject createMatch(@RequestBody @Valid MatchRequest request);
}

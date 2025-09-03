package com.example.matchContract.controller.rest;

import com.example.matchContract.dto.request.MatchContractRequest;
import com.example.matchContract.helper.ResponseObject;
import com.example.matchContract.service.MatchContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.matchContract.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class MatchContractController {
    MatchContractService matchContractService;

    @PostMapping(value = "/internal/match-contracts")
    public ResponseEntity<ResponseObject> createMatchContract(@RequestBody MatchContractRequest matchContractRequest) {
        return buildResponse(HttpStatus.CREATED, "Match contract created success", matchContractService.create(matchContractRequest));
    }
}

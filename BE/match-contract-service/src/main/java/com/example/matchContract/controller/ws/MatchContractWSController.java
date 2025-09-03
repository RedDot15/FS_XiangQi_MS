package com.example.matchContract.controller.ws;

import com.example.matchContract.dto.request.ContractAcceptRequest;
import com.example.matchContract.service.MatchContractService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class MatchContractWSController {
    MatchContractService matchContractService;

    @MessageMapping("/match-contract.accept")
    public void accept(@Valid ContractAcceptRequest request) {
        // Accept contract
        matchContractService.accept(request);
    }
}

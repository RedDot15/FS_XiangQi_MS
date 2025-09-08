package com.example.match.controller.ws;

import com.example.match.dto.request.MoveRequest;
import com.example.match.dto.request.ResignRequest;
import com.example.match.service.MatchService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class MatchWSController {
    MatchService matchService;

    @MessageMapping("/match.make-move")
    public void updateMatch(@Valid MoveRequest moveRequest) {
        // Handle move
        matchService.move(moveRequest);
    }

    @MessageMapping("/match.resign")
    public void updateMatch(@Valid ResignRequest resignRequest) {
        // Handle resign request
        matchService.resign(resignRequest);
    }
}

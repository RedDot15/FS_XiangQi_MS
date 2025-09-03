package com.example.match.controller.rest;

import com.example.match.dto.request.MatchRequest;
import com.example.match.helper.ResponseObject;
import com.example.match.service.my_sql.MatchService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.match.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/matches")
public class MatchController {
    MatchService matchService;

    @GetMapping("/{matchId}")
    public ResponseEntity<ResponseObject> getMatch(@PathVariable String matchId) {
        // Fetch board state
        return buildResponse(HttpStatus.OK, "Board state fetch successfully.", matchService.getMatchStateById(matchId));
    }

    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createMatch(@RequestBody @Valid MatchRequest request) {
        // Create new match
        return buildResponse(HttpStatus.OK, "Create new match successfully.", matchService.createMatch(request));
    }
}

package com.example.onlineUser.controller.rest;

import com.example.onlineUser.helper.ResponseObject;
import com.example.onlineUser.service.OnlineUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.onlineUser.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/online-users")
public class OnlineUserController {
    OnlineUserService onlineUserService;

    @GetMapping("/{displayedName}")
    public ResponseEntity<ResponseObject> getByUsername(@PathVariable String displayedName) {
        // Find & Return player
        return buildResponse(HttpStatus.OK, "Find user success.", onlineUserService.getByDisplayedName(displayedName));
    }
}
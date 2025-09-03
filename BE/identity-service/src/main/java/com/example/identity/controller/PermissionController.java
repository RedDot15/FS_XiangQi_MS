package com.example.identity.controller;

import com.example.identity.dto.request.PermissionRequest;
import com.example.identity.helper.ResponseObject;
import com.example.identity.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static com.example.identity.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/permissions")
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAll() {
        return buildResponse(HttpStatus.OK, "Fetch permission successfully.", permissionService.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody PermissionRequest request) {
        return buildResponse(HttpStatus.OK, "Create new permission successfully.", permissionService.create(request));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<ResponseObject> delete(@PathVariable String permissionId) {
        return buildResponse(HttpStatus.OK, "Delete permission successfully.", permissionService.delete(permissionId));
    }
}

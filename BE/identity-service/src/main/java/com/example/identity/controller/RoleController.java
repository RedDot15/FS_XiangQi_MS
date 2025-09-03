package com.example.identity.controller;

import com.example.identity.dto.request.RoleRequest;
import com.example.identity.helper.ResponseObject;
import com.example.identity.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static com.example.identity.helper.ResponseBuilder.buildResponse;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/roles")
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAll() {
        return buildResponse(HttpStatus.OK, "Fetch roles successfully.", roleService.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> create(@RequestBody RoleRequest request) {
        return buildResponse(HttpStatus.OK, "Create new role successfully.", roleService.create(request));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<ResponseObject> delete(@PathVariable String roleId) {
        return buildResponse(HttpStatus.OK, "Delete role successfully.", roleService.delete(roleId));
    }
}

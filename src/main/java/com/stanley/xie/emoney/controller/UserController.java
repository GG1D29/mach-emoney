package com.stanley.xie.emoney.controller;

import com.stanley.xie.emoney.payload.CreateUserRequest;
import com.stanley.xie.emoney.payload.CreateUserResponse;
import com.stanley.xie.emoney.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        String token = userRegistrationService.register(request.getUsername());

        return ResponseEntity.ok(new CreateUserResponse(token));
    }

}

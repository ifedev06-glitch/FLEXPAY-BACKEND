package com.json.flexpay.controller;

import com.json.flexpay.dto.AuthRequest;
import com.json.flexpay.dto.AuthResponse;
import com.json.flexpay.dto.UserDto;
import com.json.flexpay.entity.User;
import com.json.flexpay.helper.AccountHelper;
import com.json.flexpay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountHelper accountHelper;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticateUser(request);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + response.getToken())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .body(response);
    }
}

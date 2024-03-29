package com.app.controller.auth;

import com.app.controller.auth.dto.AuthLogin;
import com.app.controller.auth.dto.AuthResponse;
import com.app.controller.constants.RestConstants;
import com.app.services.users.ILoginUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(RestConstants.AUTH_RESOURCE)
public class AuthController {

    private final ILoginUserService loginUser;

    @PostMapping(path = {"/log-in"})
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLogin userRequest) {
        return new ResponseEntity<>(loginUser.login(userRequest), HttpStatus.OK);
    }
}

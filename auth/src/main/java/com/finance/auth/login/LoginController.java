package com.finance.auth.login;

import com.finance.auth.login.dto.SignInResponse;
import com.finance.auth.login.dto.SignInRequest;
import com.finance.auth.login.dto.SignUpRequest;
import com.finance.auth.login.dto.SignUpResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/signin")
    public SignInResponse signIn(@Valid @RequestBody SignInRequest request) {
        return loginService.signIn(request);
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return loginService.signUp(request);
    }
}

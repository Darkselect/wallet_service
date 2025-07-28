package uz.wallet.wallet_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.service.auth.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/admin/login")
    public JwtResponse authenticateSuperAdmin(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateAdmin(loginRequest);
    }
}
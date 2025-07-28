package uz.wallet.wallet_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.service.user.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto registerUser(@Valid @RequestBody UserCreateRequestDto dto) {
        return userService.registerUser(dto);
    }
}

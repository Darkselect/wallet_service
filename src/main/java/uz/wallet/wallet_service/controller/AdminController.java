package uz.wallet.wallet_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.request.UserUpdateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.service.admin.AdminService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Validated
public class AdminController {

    private final AdminService adminService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new-users")
    public UserResponseDto createNewUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return adminService.createNewUser(userCreateRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userId}")
    public UserResponseDto updateUserById(@PathVariable UUID userId, @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return adminService.updateUserById(userId, userUpdateRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable UUID userId) {
        return adminService.getUserById(userId);
    }
}

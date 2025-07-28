package uz.wallet.wallet_service.service.admin;

import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.request.UserUpdateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;

import java.util.UUID;

public interface AdminService {
    UserResponseDto createNewUser(UserCreateRequestDto userCreateRequestDto);
    UserResponseDto updateUserById(UUID userId, UserUpdateRequestDto userUpdateRequestDto);
    UserResponseDto getUserById(UUID userId);
}

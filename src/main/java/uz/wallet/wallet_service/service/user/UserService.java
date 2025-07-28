package uz.wallet.wallet_service.service.user;

import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;

public interface UserService {
   UserResponseDto registerUser(UserCreateRequestDto dto);
}

package uz.wallet.wallet_service.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.request.UserUpdateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserRole;
import uz.wallet.wallet_service.mapper.UserMapper;
import uz.wallet.wallet_service.service.operation.UserOperationService;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserOperationService userOperationService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserCreateRequestDto dto) {
        log.info("Создание нового пользователя: {}", dto);
        User user = buildUserFromDto(dto);
        userOperationService.saveUser(user);

        log.info("Пользователь создан: id={}, email={}", user.getId(), user.getEmail());
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponseDto updateUserById(UUID userId, UserUpdateRequestDto dto) {
        log.info("Обновление пользователя с id {}: {}", userId, dto);

        User user = userMapper.mapToUpdatedUser(dto);

        User updatedUser = userOperationService.updateUser(userId, user);

        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public UserResponseDto getUserById(UUID userId) {
        log.info("Получение пользователя с id {}", userId);
        return userMapper.toUserResponse(userOperationService.getUserFromDbAndCacheById(userId));
    }


    private User buildUserFromDto(UserCreateRequestDto dto) {
        log.debug("Преобразование DTO в сущность User: {}", dto);
        return User.builder()
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userRole(UserRole.USER)
                .balance(BigDecimal.ZERO)
                .build();
    }
}

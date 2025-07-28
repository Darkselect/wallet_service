package uz.wallet.wallet_service.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserRole;
import uz.wallet.wallet_service.mapper.UserMapper;
import uz.wallet.wallet_service.service.operation.UserOperationService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserOperationService userOperationService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto registerUser(UserCreateRequestDto dto) {
        log.info("Регистрация нового пользователя: {}", dto);
        String password = dto.getPassword();

        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(password))
                .balance(BigDecimal.ZERO)
                .userRole(UserRole.USER)
                .build();

        userOperationService.saveUser(user);

        log.info("Пользователь зарегистрирован: id={}, email={}", user.getId(), user.getEmail());
        return userMapper.toUserResponse(user);
    }
}

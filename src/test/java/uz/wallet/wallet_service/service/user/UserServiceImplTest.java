package uz.wallet.wallet_service.service.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserRole;
import uz.wallet.wallet_service.mapper.UserMapper;
import uz.wallet.wallet_service.service.operation.UserOperationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserOperationService userOperationService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("registerUser: успешная регистрация пользователя")
    void registerUser_Success() {
        UserCreateRequestDto dto = UserCreateRequestDto.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthDate(LocalDate.of(1990, 1, 15))
                .phoneNumber("+998901234567")
                .email("ivan.ivanov@example.com")
                .password("SecurePass123!")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password("encodedPassword")
                .balance(BigDecimal.ZERO)
                .userRole(UserRole.USER)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        doAnswer(invocation -> {
            User argUser = invocation.getArgument(0);
            argUser.setId(user.getId());
            return null;
        }).when(userOperationService).saveUser(any(User.class));

        when(userMapper.toUserResponse(any(User.class))).thenReturn(responseDto);

        UserResponseDto actualResponse = userService.registerUser(dto);

        assertNotNull(actualResponse);
        assertEquals(responseDto.getId(), actualResponse.getId());
        assertEquals(dto.getEmail(), actualResponse.getEmail());

        verify(passwordEncoder).encode(dto.getPassword());
        verify(userOperationService).saveUser(any(User.class));
        verify(userMapper).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("registerUser: падение при null dto")
    void registerUser_NullDto() {
        assertThrows(NullPointerException.class, () -> userService.registerUser(null));
    }

    @Test
    @DisplayName("registerUser: падение при null password")
    void registerUser_NullPassword() {
        UserCreateRequestDto dto = UserCreateRequestDto.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthDate(LocalDate.of(1990, 1, 15))
                .phoneNumber("+998901234567")
                .email("ivan.ivanov@example.com")
                .password(null)
                .build();

        when(passwordEncoder.encode(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> userService.registerUser(dto));
    }
}

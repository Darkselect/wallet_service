package uz.wallet.wallet_service.service.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.request.UserUpdateRequestDto;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserOperationService userOperationService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;


    @Test
    @DisplayName("createNewUser: успешно создает и возвращает UserResponseDto")
    void createNewUser_Success() {
        UserCreateRequestDto dto = UserCreateRequestDto.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthDate(LocalDate.of(1990, 1, 15))
                .email("ivan.ivanov@example.com")
                .phoneNumber("+998901234567")
                .password("SecurePass123!")
                .build();

        User userEntity = User.builder()
                .id(UUID.randomUUID())
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .birthDate(dto.getBirthDate())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .password("encodedPassword")
                .userRole(UserRole.USER)
                .balance(BigDecimal.ZERO)
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .build();

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        doAnswer(invocation -> {
            User argUser = invocation.getArgument(0);
            argUser.setId(userEntity.getId());
            return null;
        }).when(userOperationService).saveUser(any(User.class));

        when(userMapper.toUserResponse(any(User.class))).thenReturn(responseDto);

        UserResponseDto result = adminService.createNewUser(dto);

        assertNotNull(result);
        assertEquals(dto.getEmail(), result.getEmail());
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userOperationService).saveUser(any(User.class));
        verify(userMapper).toUserResponse(any(User.class));
    }

    @Test
    @DisplayName("updateUserById: успешно обновляет и возвращает UserResponseDto")
    void updateUserById_Success() {
        UUID userId = UUID.randomUUID();

        UserUpdateRequestDto updateDto = UserUpdateRequestDto.builder()
                .lastName("Petrov")
                .firstName("Petr")
                .middleName("Petrovich")
                .birthDate(LocalDate.of(1985, 5, 20))
                .email("petr.petrov@example.com")
                .phoneNumber("+998900000000")
                .userRole(UserRole.ADMIN)
                .build();

        User updatedUserEntity = User.builder()
                .id(userId)
                .lastName(updateDto.getLastName())
                .firstName(updateDto.getFirstName())
                .middleName(updateDto.getMiddleName())
                .birthDate(updateDto.getBirthDate())
                .email(updateDto.getEmail())
                .phoneNumber(updateDto.getPhoneNumber())
                .userRole(updateDto.getUserRole())
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(userId)
                .email(updateDto.getEmail())
                .build();

        when(userMapper.mapToUpdatedUser(updateDto)).thenReturn(updatedUserEntity);
        when(userOperationService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUserEntity);
        when(userMapper.toUserResponse(updatedUserEntity)).thenReturn(responseDto);

        UserResponseDto result = adminService.updateUserById(userId, updateDto);

        assertNotNull(result);
        assertEquals(updateDto.getEmail(), result.getEmail());
        verify(userMapper).mapToUpdatedUser(updateDto);
        verify(userOperationService).updateUser(eq(userId), any(User.class));
        verify(userMapper).toUserResponse(updatedUserEntity);
    }

    @Test
    @DisplayName("getUserById: возвращает UserResponseDto по ID")
    void getUserById_Success() {
        UUID userId = UUID.randomUUID();

        User userEntity = User.builder()
                .id(userId)
                .email("user@example.com")
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(userId)
                .email(userEntity.getEmail())
                .build();

        when(userOperationService.getUserFromDbAndCacheById(userId)).thenReturn(userEntity);
        when(userMapper.toUserResponse(userEntity)).thenReturn(responseDto);

        UserResponseDto result = adminService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userEntity.getEmail(), result.getEmail());
        verify(userOperationService).getUserFromDbAndCacheById(userId);
        verify(userMapper).toUserResponse(userEntity);
    }
}

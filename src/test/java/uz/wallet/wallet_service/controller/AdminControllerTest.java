package uz.wallet.wallet_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uz.wallet.wallet_service.dto.request.UserCreateRequestDto;
import uz.wallet.wallet_service.dto.request.UserUpdateRequestDto;
import uz.wallet.wallet_service.dto.response.UserResponseDto;
import uz.wallet.wallet_service.service.admin.AdminService;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminService adminService;

    @Test
    @DisplayName("POST /api/v1/admin/new-users - успешное создание пользователя")
    void createNewUser_Success() throws Exception {
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthDate(LocalDate.of(1990, 1, 15))
                .phoneNumber("+998901234567")
                .email("ivan.ivanov@example.com")
                .password("SecurePass123!")
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(UUID.randomUUID())
                .email(requestDto.getEmail())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .build();

        when(adminService.createNewUser(any(UserCreateRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/admin/new-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ivan.ivanov@example.com"))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"));
    }


    @Test
    @DisplayName("PUT /api/v1/admin/{userId} - успешное обновление пользователя")
    void updateUser_Success() throws Exception {
        UUID userId = UUID.randomUUID();

        var requestDto = new UserUpdateRequestDto(
                "Ivanov", "Ivan", "Ivanovich", LocalDate.of(1990,1,15), "+998901234567",
                "ivan.ivanov@example.com", uz.wallet.wallet_service.entity.UserRole.USER);

        var responseDto = UserResponseDto.builder()
                .id(userId)
                .email(requestDto.getEmail())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .build();

        when(adminService.updateUserById(any(UUID.class), any(UserUpdateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/admin/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value(requestDto.getEmail()));
    }

    @Test
    @DisplayName("GET /api/v1/admin/{userId} - успешное получение пользователя")
    void getUserById_Success() throws Exception {
        UUID userId = UUID.randomUUID();

        var responseDto = UserResponseDto.builder()
                .id(userId)
                .email("ivan.ivanov@example.com")
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();

        when(adminService.getUserById(userId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/admin/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("ivan.ivanov@example.com"));
    }
}

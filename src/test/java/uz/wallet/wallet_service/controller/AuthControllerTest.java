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
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.service.auth.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("POST /api/v1/auth/login - успешная аутентификация пользователя")
    void authenticateUser_Success() throws Exception {
        LoginRequest request = new LoginRequest("user@example.com", "userpass123");
        JwtResponse response = JwtResponse.builder()
                .token("mock-jwt-token")
                .role(null)
                .build();

        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }


    @Test
    @DisplayName("POST /api/v1/auth/admin/login - успешная аутентификация админа")
    void authenticateAdmin_Success() throws Exception {
        LoginRequest request = new LoginRequest("admin@example.com", "adminpass123");
        JwtResponse response = JwtResponse.builder()
                .token("mock-admin-jwt-token")
                .role(null)
                .build();

        when(authService.authenticateAdmin(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-admin-jwt-token"));
    }
}

package uz.wallet.wallet_service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.exception.AuthenticationFailedException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserAuthService userAuthService;
    private final AdminAuthService adminAuthService;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        log.info("Попытка аутентификации пользователя с почтой: {}", email);
        try {
            JwtResponse response = userAuthService.authenticate(loginRequest);
            log.info("Пользователь успешно аутентифицирован: {}", email);
            return response;
        } catch (Exception ex) {
            log.error("Ошибка аутентификации пользователя с почтой {}: {}", email, ex.getMessage(), ex);
            throw new AuthenticationFailedException(email, ex);
        }
    }

    public JwtResponse authenticateAdmin(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        log.info("Попытка аутентификации супер-администратора с почтой: {}", email);
        try {
            JwtResponse response = adminAuthService.authenticate(loginRequest);
            log.info("Админ успешно аутентифицирован: {}", email);
            return response;
        } catch (Exception ex) {
            log.error("Ошибка аутентификации Админа с почтой {}: {}", email, ex.getMessage(), ex);
            throw new AuthenticationFailedException(email, ex);
        }
    }
}

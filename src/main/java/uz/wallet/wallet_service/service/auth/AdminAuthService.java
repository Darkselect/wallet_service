package uz.wallet.wallet_service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.exception.AuthenticationFailedException;
import uz.wallet.wallet_service.service.impl.admin.AdminDetailsServiceImpl;
import uz.wallet.wallet_service.utill.AuthenticationUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminDetailsServiceImpl adminDetailsService;
    private final AuthenticationUtils authenticationUtils;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        log.info("Аутентификация админа: {}", email);

        try {
            UserDetails userDetails = adminDetailsService.loadUserByUsername(email);
            JwtResponse jwtResponse = authenticationUtils.performAuthentication(loginRequest, userDetails);
            log.info("Аутентификация админа успешна: {}", email);
            return jwtResponse;
        } catch (Exception ex) {
            log.error("Аутентификация админа не удалась: {} — {}", email, ex.getMessage(), ex);
            throw new AuthenticationFailedException(email, ex);
        }
    }
}
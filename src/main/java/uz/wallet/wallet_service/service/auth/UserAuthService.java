package uz.wallet.wallet_service.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.exception.AuthenticationFailedException;
import uz.wallet.wallet_service.service.impl.UserDetailsServiceImpl;
import uz.wallet.wallet_service.utill.AuthenticationUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationUtils authenticationUtils;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        log.info("Аутентификация пользователя: {}", email);

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            JwtResponse jwtResponse = authenticationUtils.performAuthentication(loginRequest, userDetails);
            log.info("Пользователь аутентифицирован: {}", email);
            return jwtResponse;
        } catch (AuthenticationFailedException ex) {
            log.error("Ошибка при аутентификации пользователя по электронной почте: {} — {}", email, ex.getMessage(), ex);
            throw new AuthenticationFailedException(email, ex);
        }
    }
}

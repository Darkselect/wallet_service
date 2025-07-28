package uz.wallet.wallet_service.utill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.wallet.wallet_service.dto.request.JwtResponse;
import uz.wallet.wallet_service.dto.request.LoginRequest;
import uz.wallet.wallet_service.entity.UserRole;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationUtils {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponse performAuthentication(LoginRequest loginRequest, UserDetails userDetails) {
        log.info("Authenticating user with email: {}", loginRequest.getEmail());

        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            log.warn("Authentication failed for email: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }

        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword());
        log.info("Результат matches: {}", matches);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String jwt = jwtUtils.generateJwtToken(authentication);
        String authority = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow();

        UserRole role = UserRole.valueOf(authority);

        log.info("Authentication successful for user: {}, role: {}", loginRequest.getEmail(), role);

        return JwtResponse.builder()
                .token(jwt)
                .role(role)
                .build();
    }
}

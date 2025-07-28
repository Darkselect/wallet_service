package uz.wallet.wallet_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.wallet.wallet_service.exception.UserNotFoundException;
import uz.wallet.wallet_service.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", email);

        return userRepository.findByEmail(email)
                .map(user -> {
                    log.info("Пользователь найден: {}", email);
                    return UserDetailsImpl.build(user);
                })
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", email);
                    return new UserNotFoundException(String.format("User with email %s not found", email));
                });
    }
}
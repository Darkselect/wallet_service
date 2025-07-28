package uz.wallet.wallet_service.service.impl.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.wallet.wallet_service.entity.Admin;
import uz.wallet.wallet_service.exception.UserNotFoundException;
import uz.wallet.wallet_service.repository.AdminRepository;

@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminRepository superAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = superAdminRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(String.format("Админ с почтой %s не найден", email)));

        return AdminDetailsImpl.buildSuperAdmin(admin);
    }
}

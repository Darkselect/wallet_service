package uz.wallet.wallet_service.service.impl.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.wallet.wallet_service.entity.Admin;
import uz.wallet.wallet_service.entity.UserRole;
import uz.wallet.wallet_service.security.HasAuthContext;
import uz.wallet.wallet_service.service.impl.HasId;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class AdminDetailsImpl implements UserDetails, HasId, HasAuthContext {

    @Getter
    private final UUID id;

    private final String email;
    private final String password;

    @Getter
    private final UserRole userRole;

    private final List<GrantedAuthority> authorities;

    public static AdminDetailsImpl buildSuperAdmin(Admin admin) {
        String password = admin.getPassword() != null ? admin.getPassword() : "";
        UserRole role = admin.getUserRole() != null ? admin.getUserRole() : UserRole.ADMIN;

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

        return new AdminDetailsImpl(
                admin.getId(),
                admin.getEmail(),
                password,
                role,
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

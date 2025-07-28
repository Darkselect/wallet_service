package uz.wallet.wallet_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserRole;
import uz.wallet.wallet_service.security.HasAuthContext;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails, HasId, HasAuthContext {
    @Getter
    private final UUID id;
    private final String email;
    private final String password;

    @Getter
    private UserRole userRole;

    private final Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        String password = user.getPassword() != null ? user.getPassword() : "";
        UserRole role = user.getUserRole();
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                password,
                role,
                List.of(new SimpleGrantedAuthority(role.name()))
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
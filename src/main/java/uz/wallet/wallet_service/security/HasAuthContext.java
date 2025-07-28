package uz.wallet.wallet_service.security;

import uz.wallet.wallet_service.entity.UserRole;

import java.util.UUID;

public interface HasAuthContext {
    UUID getId();
    UserRole getUserRole();
}
package uz.wallet.wallet_service.service.cache;

import uz.wallet.wallet_service.entity.cacheEntity.UserCacheEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserCacheService {
    Optional<UserCacheEntity> findUserById(UUID id);
    void cacheUser(UserCacheEntity user);
    void cacheUsers(List<UserCacheEntity> users);
    void removeUserFromCache(UUID userId);
}

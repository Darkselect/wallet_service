package uz.wallet.wallet_service.repository.cache;

import org.springframework.data.repository.CrudRepository;
import uz.wallet.wallet_service.entity.cacheEntity.UserCacheEntity;

import java.util.UUID;

public interface UserCacheRepository extends CrudRepository<UserCacheEntity, UUID> {
}

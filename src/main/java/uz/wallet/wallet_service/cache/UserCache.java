package uz.wallet.wallet_service.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.cacheEntity.UserCacheEntity;
import uz.wallet.wallet_service.mapper.UserCacheMapper;
import uz.wallet.wallet_service.repository.UserRepository;
import uz.wallet.wallet_service.service.cache.UserCacheServiceImpl;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class UserCache extends AbstractCacheWarmup<User, UserCacheEntity> {
    private static final String USER_CACHE = "userCache";

    private final UserCacheServiceImpl userCacheServiceImpl;
    private final UserRepository userRepository;
    private final UserCacheMapper userCacheMapper;

    public UserCache(@Qualifier("async") ThreadPoolTaskExecutor taskExecutor, UserCacheServiceImpl userCacheServiceImpl,
                     UserRepository userRepository, UserCacheMapper userCacheMapper) {
        super(taskExecutor);
        this.userCacheServiceImpl = userCacheServiceImpl;
        this.userRepository = userRepository;
        this.userCacheMapper = userCacheMapper;
    }

    @Override
    protected List<User> fetchBatch(UUID lastId, int batchSize) {
        return userRepository.findUsersByBatch(lastId, batchSize);
    }

    @Override
    protected UUID getLastId(List<User> entities) {
        return entities.getLast().getId();
    }

    @Override
    protected UserCacheEntity mapToCacheEntity(User entity) {
        return userCacheMapper.toUserCache(entity);
    }

    @Override
    protected void saveCache(List<UserCacheEntity> cacheEntities) {
        userCacheServiceImpl.cacheUsers(cacheEntities);
    }

    @Override
    protected String getCacheName() {
        return USER_CACHE;
    }
}
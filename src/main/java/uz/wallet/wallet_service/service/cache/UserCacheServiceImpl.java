package uz.wallet.wallet_service.service.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.wallet.wallet_service.entity.cacheEntity.UserCacheEntity;
import uz.wallet.wallet_service.exception.UserNotFoundException;
import uz.wallet.wallet_service.repository.cache.UserCacheRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheServiceImpl implements UserCacheService {

    private final UserCacheRepository userCacheRepository;

    @Override
    public Optional<UserCacheEntity> findUserById(UUID id) {
        log.info("Поиск пользователя в кэше по ID: {}", id);
        try {
            Optional<UserCacheEntity> result = userCacheRepository.findById(id);
            log.info("Пользователь по ID {} найден в кэше: {}", id, result.isPresent());
            return result;
        } catch (UserNotFoundException e) {
            log.warn("Пользователь не найден в кэше по ID: {}", id);
            return Optional.empty();
        } catch (Exception e) {
            log.error("Неожиданная ошибка при поиске пользователя по ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void cacheUser(UserCacheEntity user) {
        if (user != null && user.getId() != null) {
            UUID id = user.getId();
            log.info("Кэширование одного пользователя: {}", id);
            try {
                userCacheRepository.save(user);
                log.info("Пользователь успешно закэширован: {}", id);
            } catch (Exception e) {
                log.error("Ошибка при кэшировании пользователя: {}", id, e);
                throw new RuntimeException(String.format("Ошибка при кэшировании пользователя: %s", id), e);
            }
        } else {
            log.warn("Пропуск кэширования — пользователь или ID = null");
        }
    }

    @Override
    public void cacheUsers(List<UserCacheEntity> users) {
        log.info("Кэширование {} пользователей", users != null ? users.size() : 0);
        for (UserCacheEntity userFromCache : Objects.requireNonNull(users)) {
            if (userFromCache != null && userFromCache.getId() != null) {
                UUID id = userFromCache.getId();
                try {
                    userCacheRepository.save(userFromCache);
                    log.info("Пользователь закэширован: {}", id);
                } catch (Exception e) {
                    log.error("Ошибка при кэшировании пользователя в пакете: {}", id, e);
                    throw new RuntimeException(String.format("Ошибка при кэшировании пользователей: %s", id), e);
                }
            } else {
                log.warn("Пропуск — некорректный или null пользователь при кэшировании списка");
            }
        }
    }

    @Override
    public void removeUserFromCache(UUID userId) {
        log.info("Удаление пользователя из кэша: {}", userId);
        try {
            userCacheRepository.deleteById(userId);
            log.info("Пользователь успешно удалён из кэша: {}", userId);
        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя из кэша: {}", userId, e);
            throw new RuntimeException(String.format("Ошибка при удалении пользователя из кэша: %s", userId), e);
        }
    }
}

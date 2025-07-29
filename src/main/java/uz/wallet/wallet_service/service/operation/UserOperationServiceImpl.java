package uz.wallet.wallet_service.service.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.exception.UserNotFoundException;
import uz.wallet.wallet_service.mapper.UserCacheMapper;
import uz.wallet.wallet_service.repository.UserRepository;
import uz.wallet.wallet_service.service.cache.UserCacheService;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserOperationServiceImpl implements UserOperationService {

    private final UserRepository userRepository;
    private final UserCacheService userCacheService;
    private final UserCacheMapper userCacheMapper;

    public void saveUser(User user) {
        log.info("Сохранение пользователя: {}", user);
        cacheUser(user);
        userRepository.save(user);
    }

    @Override
    public User updateUser(UUID userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с id %s не нашелся", userId)));

        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setUserRole(updatedUser.getUserRole());

        log.info("Updating user: {}", existingUser);
        return userRepository.save(existingUser);
    }

    public User getUserFromDbAndCacheById(UUID userId) {
        log.debug("Получение пользователя с ID: {}", userId);
        return userCacheService.findUserById(userId)
                .map(userCacheMapper::toUserEntity)
                .orElseGet(() -> {
                    log.debug("Пользователь не нашелся в кэше. Получение пользователя из базы данных: {}", userId);
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> {
                                log.warn("Пользователь не нашелся в базе данных: {}", userId);
                                return new UserNotFoundException("Пользователь не найден");
                            });
                    cacheUser(user);
                    return user;
                });
    }

    public void cacheUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
        log.debug("Кэширование пользователя: {}", user.getId());
        userCacheService.cacheUser(userCacheMapper.toUserCache(user));
    }
}

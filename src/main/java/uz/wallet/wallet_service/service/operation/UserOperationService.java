package uz.wallet.wallet_service.service.operation;

import uz.wallet.wallet_service.entity.User;

import java.util.UUID;

public interface UserOperationService {
    void saveUser(User user);
    User updateUser(UUID userId, User updatedUser);
    User getUserFromDbAndCacheById(UUID userId);
}

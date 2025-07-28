package uz.wallet.wallet_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.wallet.wallet_service.entity.UserTransaction;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserTransactionRepository extends JpaRepository<UserTransaction, UUID> {
    Page<UserTransaction> findAllBySenderIdOrReceiverId(UUID senderId, UUID receiverId, Pageable pageable);
    Page<UserTransaction> findAllByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}

package uz.wallet.wallet_service.service.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.wallet.wallet_service.dto.request.UserTransactionCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserTransactionResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserTransactionService {
    UserTransactionResponse createTransaction(UserTransactionCreateRequestDto request);
    Page<UserTransactionResponse> getAllTransactions(Pageable pageable);
    Page<UserTransactionResponse> getTransactionsByUserId(UUID userId, Pageable pageable);
    Page<UserTransactionResponse> getTransactionsByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable);
}

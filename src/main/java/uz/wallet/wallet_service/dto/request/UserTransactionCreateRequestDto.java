package uz.wallet.wallet_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.wallet.wallet_service.entity.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTransactionCreateRequestDto {
    private UUID senderId;
    private UUID receiverId;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;
}

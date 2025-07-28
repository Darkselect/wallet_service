package uz.wallet.wallet_service.entity.cacheEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import uz.wallet.wallet_service.entity.UserRole;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@RedisHash(value = "user_from_cache", timeToLive = 12000)
public class UserCacheEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private UserRole userRole;
}

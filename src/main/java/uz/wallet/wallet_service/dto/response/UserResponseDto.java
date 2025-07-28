package uz.wallet.wallet_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String userRole;
}

package uz.wallet.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.wallet.wallet_service.dto.request.UserTransactionCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserTransactionResponse;
import uz.wallet.wallet_service.entity.UserTransaction;

@Mapper(componentModel = "spring")
public interface UserTransactionMapper {

    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    UserTransaction toEntity(UserTransactionCreateRequestDto request);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    UserTransactionResponse toDto(UserTransaction transaction);
}



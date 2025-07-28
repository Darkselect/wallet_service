package uz.wallet.wallet_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.cacheEntity.UserCacheEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCacheMapper {
    @Mapping(source = "id", target = "id")
    UserCacheEntity toUserCache(User user);

    @Mapping(source = "id", target = "id")
    User toUserEntity(UserCacheEntity cacheEntity);
}
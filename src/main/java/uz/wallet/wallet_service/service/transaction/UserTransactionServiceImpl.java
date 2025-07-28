package uz.wallet.wallet_service.service.transaction;// TransactionServiceImpl.java

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.wallet.wallet_service.dto.request.UserTransactionCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserTransactionResponse;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserTransaction;
import uz.wallet.wallet_service.mapper.UserTransactionMapper;
import uz.wallet.wallet_service.repository.UserTransactionRepository;
import uz.wallet.wallet_service.service.operation.UserOperationService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTransactionServiceImpl implements UserTransactionService {

    private final UserTransactionRepository userTransactionRepository;
    private final UserTransactionMapper userTransactionMapper;
    private final UserOperationService userOperationService;

    @Override
    @Transactional
    public UserTransactionResponse createTransaction(UserTransactionCreateRequestDto request) {
        log.info("Создание транзакции между пользователями: отправитель={}, получатель={}, сумма={}",
                request.getSenderId(), request.getReceiverId(), request.getAmount());

        UserTransaction transaction = userTransactionMapper.toEntity(request);

        User sender = userOperationService.getUserFromDbAndCacheById(request.getSenderId());
        log.debug("Отправитель загружен: {}", sender.getId());

        User receiver = userOperationService.getUserFromDbAndCacheById(request.getReceiverId());
        log.debug("Получатель загружен: {}", receiver.getId());

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setCreatedAt(LocalDateTime.now());

        UserTransaction saved = userTransactionRepository.save(transaction);
        log.info("Транзакция успешно сохранена: id={}, сумма={}, тип={}",
                saved.getId(), saved.getAmount(), saved.getTransactionType());

        return userTransactionMapper.toDto(saved);
    }

    @Override
    public Page<UserTransactionResponse> getAllTransactions(Pageable pageable) {
        log.info("Получение всех транзакций: страница={}, размер={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<UserTransactionResponse> page = userTransactionRepository.findAll(pageable)
                .map(userTransactionMapper::toDto);
        log.debug("Получено {} транзакций", page.getNumberOfElements());
        return page;
    }

    @Override
    public Page<UserTransactionResponse> getTransactionsByUserId(UUID userId, Pageable pageable) {
        log.info("Получение транзакций по пользователю: userId={}, страница={}, размер={}", userId, pageable.getPageNumber(), pageable.getPageSize());
        Page<UserTransactionResponse> page = userTransactionRepository
                .findAllBySenderIdOrReceiverId(userId, userId, pageable)
                .map(userTransactionMapper::toDto);
        log.debug("Получено {} транзакций по пользователю {}", page.getNumberOfElements(), userId);
        return page;
    }

    @Override
    public Page<UserTransactionResponse> getTransactionsByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        log.info("Получение транзакций по интервалу дат: от {} до {}, страница={}, размер={}", from, to, pageable.getPageNumber(), pageable.getPageSize());
        Page<UserTransactionResponse> page = userTransactionRepository
                .findAllByCreatedAtBetween(from, to, pageable)
                .map(userTransactionMapper::toDto);
        log.debug("Получено {} транзакций за указанный период", page.getNumberOfElements());
        return page;
    }

}

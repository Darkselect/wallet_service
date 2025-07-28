package uz.wallet.wallet_service.service.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uz.wallet.wallet_service.dto.request.UserTransactionCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserTransactionResponse;
import uz.wallet.wallet_service.entity.User;
import uz.wallet.wallet_service.entity.UserTransaction;
import uz.wallet.wallet_service.mapper.UserTransactionMapper;
import uz.wallet.wallet_service.repository.UserTransactionRepository;
import uz.wallet.wallet_service.service.operation.UserOperationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserTransactionServiceImplTest {

    @Mock
    private UserTransactionRepository userTransactionRepository;

    @Mock
    private UserTransactionMapper userTransactionMapper;

    @Mock
    private UserOperationService userOperationService;

    @InjectMocks
    private UserTransactionServiceImpl userTransactionService;

    @Test
    @DisplayName("createTransaction: успешное создание транзакции")
    void createTransaction_Success() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        UserTransactionCreateRequestDto requestDto = UserTransactionCreateRequestDto.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .amount(new BigDecimal("100.00"))
                .build();

        User sender = User.builder().id(senderId).build();
        User receiver = User.builder().id(receiverId).build();

        UserTransaction transaction = UserTransaction.builder().build();
        UserTransaction savedTransaction = UserTransaction.builder()
                .id(UUID.randomUUID())
                .amount(requestDto.getAmount())
                .build();

        UserTransactionResponse responseDto = UserTransactionResponse.builder()
                .id(savedTransaction.getId())
                .amount(savedTransaction.getAmount())
                .build();

        when(userTransactionMapper.toEntity(requestDto)).thenReturn(transaction);
        when(userOperationService.getUserFromDbAndCacheById(senderId)).thenReturn(sender);
        when(userOperationService.getUserFromDbAndCacheById(receiverId)).thenReturn(receiver);
        when(userTransactionRepository.save(any(UserTransaction.class))).thenReturn(savedTransaction);
        when(userTransactionMapper.toDto(savedTransaction)).thenReturn(responseDto);

        UserTransactionResponse actualResponse = userTransactionService.createTransaction(requestDto);

        assertNotNull(actualResponse);
        assertEquals(responseDto.getId(), actualResponse.getId());
        assertEquals(responseDto.getAmount(), actualResponse.getAmount());

        verify(userTransactionMapper).toEntity(requestDto);
        verify(userOperationService).getUserFromDbAndCacheById(senderId);
        verify(userOperationService).getUserFromDbAndCacheById(receiverId);
        verify(userTransactionRepository).save(any(UserTransaction.class));
        verify(userTransactionMapper).toDto(savedTransaction);
    }

    @Test
    @DisplayName("getAllTransactions: успешное получение страницы транзакций")
    void getAllTransactions_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        UserTransaction transaction = UserTransaction.builder().id(UUID.randomUUID()).build();
        UserTransactionResponse responseDto = UserTransactionResponse.builder().id(transaction.getId()).build();

        Page<UserTransaction> page = new PageImpl<>(List.of(transaction), pageable, 1);

        when(userTransactionRepository.findAll(pageable)).thenReturn(page);
        when(userTransactionMapper.toDto(transaction)).thenReturn(responseDto);

        Page<UserTransactionResponse> actualPage = userTransactionService.getAllTransactions(pageable);

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getTotalElements());
        assertEquals(responseDto.getId(), actualPage.getContent().get(0).getId());

        verify(userTransactionRepository).findAll(pageable);
        verify(userTransactionMapper).toDto(transaction);
    }

    @Test
    @DisplayName("getTransactionsByUserId: успешное получение транзакций по пользователю")
    void getTransactionsByUserId_Success() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        UserTransaction transaction = UserTransaction.builder().id(UUID.randomUUID()).build();
        UserTransactionResponse responseDto = UserTransactionResponse.builder().id(transaction.getId()).build();

        Page<UserTransaction> page = new PageImpl<>(List.of(transaction), pageable, 1);

        when(userTransactionRepository.findAllBySenderIdOrReceiverId(userId, userId, pageable)).thenReturn(page);
        when(userTransactionMapper.toDto(transaction)).thenReturn(responseDto);

        Page<UserTransactionResponse> actualPage = userTransactionService.getTransactionsByUserId(userId, pageable);

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getTotalElements());
        assertEquals(responseDto.getId(), actualPage.getContent().get(0).getId());

        verify(userTransactionRepository).findAllBySenderIdOrReceiverId(userId, userId, pageable);
        verify(userTransactionMapper).toDto(transaction);
    }

    @Test
    @DisplayName("getTransactionsByDateRange: успешное получение транзакций по дате")
    void getTransactionsByDateRange_Success() {
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        UserTransaction transaction = UserTransaction.builder().id(UUID.randomUUID()).build();
        UserTransactionResponse responseDto = UserTransactionResponse.builder().id(transaction.getId()).build();

        Page<UserTransaction> page = new PageImpl<>(List.of(transaction), pageable, 1);

        when(userTransactionRepository.findAllByCreatedAtBetween(from, to, pageable)).thenReturn(page);
        when(userTransactionMapper.toDto(transaction)).thenReturn(responseDto);

        Page<UserTransactionResponse> actualPage = userTransactionService.getTransactionsByDateRange(from, to, pageable);

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getTotalElements());
        assertEquals(responseDto.getId(), actualPage.getContent().get(0).getId());

        verify(userTransactionRepository).findAllByCreatedAtBetween(from, to, pageable);
        verify(userTransactionMapper).toDto(transaction);
    }
}

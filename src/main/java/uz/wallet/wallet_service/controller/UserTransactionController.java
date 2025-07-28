package uz.wallet.wallet_service.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uz.wallet.wallet_service.dto.request.UserTransactionCreateRequestDto;
import uz.wallet.wallet_service.dto.response.UserTransactionResponse;
import uz.wallet.wallet_service.service.transaction.UserTransactionService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Validated
public class UserTransactionController {

    private final UserTransactionService userTransactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTransactionResponse createTransaction(@Valid @RequestBody UserTransactionCreateRequestDto request) {
        return userTransactionService.createTransaction(request);
    }

    @GetMapping
    public Page<UserTransactionResponse> getAllTransactions(Pageable pageable) {
        return userTransactionService.getAllTransactions(pageable);
    }

    @GetMapping("/by-user")
    public Page<UserTransactionResponse> getTransactionsByUserId(@RequestParam @NotNull UUID userId, Pageable pageable) {
        return userTransactionService.getTransactionsByUserId(userId, pageable);
    }

    @GetMapping("/by-dates")
    public Page<UserTransactionResponse> getTransactionsByDateRange(
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable
    ) {
        return userTransactionService.getTransactionsByDateRange(from, to, pageable);
    }
}

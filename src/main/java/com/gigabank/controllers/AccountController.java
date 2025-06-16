package com.gigabank.controllers;

import com.gigabank.models.dto.request.account.ChangeStatusAccountRequest;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.service.account.AccountService;
import com.gigabank.service.account.BankOperationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST контроллер для управления банковскими счетами.
 * Предоставляет API для создания, управления и получения информации о счетах.
 * Все методы требуют аутентификации и соответствующих прав доступа.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final BankOperationService bankOperationService;

    /**
     * Создает новый банковский счет для указанного пользователя.
     *
     * @param request DTO с данными для создания счета
     * @return DTO созданного счета
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDto createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        return accountService.createAccount(request);
    }

    /**
     * Получает полную информацию о счете по его идентификатору.
     *
     * @param id уникальный идентификатор счета
     * @return DTO с информацией о счете
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDto getAccountById(@PathVariable Long id) {
        return accountService.getAccountByIdFromController(id);
    }

    /**
     * Получает список всех транзакций по указанному счету.
     *
     * @param id уникальный идентификатор счета
     * @return список DTO транзакций
     */
    @GetMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionResponseDto> getAccountTransactions(@PathVariable Long id,
                                                               Pageable pageable) {
        return accountService.getTransactionsByAccountIdAndPageable(id, pageable);
    }

    /**
     * Выполняет списание средств с указанного счета.
     *
     * @param id     уникальный идентификатор счета
     * @param amount сумма для списания (должна быть положительной)
     */
    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@PathVariable Long id, @RequestParam @PositiveOrZero BigDecimal amount) {
        bankOperationService.withdraw(id, amount);
    }

    /**
     * Выполняет пополнение указанного счета.
     *
     * @param id     уникальный идентификатор счета
     * @param amount сумма для пополнения (должна быть положительной)
     */
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@PathVariable Long id, @RequestParam @PositiveOrZero BigDecimal amount) {
        bankOperationService.deposit(id, amount);
    }

    /**
     * Изменяет статус счета и транзакций по идентификатору.
     *
     * @param id      идентификатор счета
     * @param request DTO с новым статусом и причиной изменения
     */
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeAccountStatus(@PathVariable Long id,
                                    @Valid @RequestBody ChangeStatusAccountRequest request) {
        accountService.changeAccountStatus(id, request.getStatus());
    }
}
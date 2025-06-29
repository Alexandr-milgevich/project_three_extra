package com.gigabank.controllers;

import com.gigabank.models.dto.request.account.ChangeStatusBankAccountRequest;
import com.gigabank.models.dto.request.account.CreateBankAccountRequestDto;
import com.gigabank.models.dto.request.operation.TransferRequestDto;
import com.gigabank.models.dto.response.BankAccountResponseDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.service.account.BankAccountOperationService;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.service.status.BankAccountStatusService;
import com.gigabank.service.transaction.TransactionService;
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
public class BankAccountController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final BankAccountStatusService bankAccountStatusService;
    private final BankAccountOperationService bankAccountOperationService;

    /**
     * Создает новый банковский счет для указанного пользователя.
     *
     * @param request DTO с данными для создания счета
     * @return DTO созданного счета
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponseDto createAccount(@Valid @RequestBody CreateBankAccountRequestDto request) {
        return bankAccountService.createAccount(request);
    }

    /**
     * Получает полную информацию о счете по его идентификатору.
     *
     * @param id уникальный идентификатор счета
     * @return DTO с информацией о счете
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankAccountResponseDto getAccountById(@PathVariable Long id) {
        return bankAccountService.getAccountByIdFromController(id);
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
        return transactionService.getTransactionsByAccountIdAndPageable(id, pageable);
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
                                    @Valid @RequestBody ChangeStatusBankAccountRequest request) {
        bankAccountStatusService.changeStatus(id, request.getStatus());
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
        bankAccountOperationService.withdraw(id, amount);
    }

    /**
     * Выполняет пополнение средств для указанного счета.
     *
     * @param id     уникальный идентификатор счета
     * @param amount сумма для пополнения (должна быть положительной)
     */
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@PathVariable Long id, @RequestParam @PositiveOrZero BigDecimal amount) {
        bankAccountOperationService.deposit(id, amount);
    }

    /**
     * Выполняет перевод средств между счетами
     *
     * @param request DTO с данными для перевода.
     */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferBetweenAccounts(
            @RequestBody @Valid TransferRequestDto request) {
        bankAccountOperationService.transferBetweenAccounts(
                request.getSourceUserId(),
                request.getTargetUserId(),
                request.getAmount());
    }
}
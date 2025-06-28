package com.gigabank.controllers;

import com.gigabank.models.dto.request.account.ChangeStatusBankAccountRequest;
import com.gigabank.models.dto.request.account.CreateBankAccountRequestDto;
import com.gigabank.models.dto.request.operation.OperationRequestDto;
import com.gigabank.models.dto.request.operation.TransferRequestDto;
import com.gigabank.models.dto.response.BankAccountResponseDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.service.account.BankOperationService;
import com.gigabank.service.status.BankAccountStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления банковскими счетами.
 * Предоставляет API для создания, управления и получения информации о счетах.
 * Все методы требуют аутентификации и соответствующих прав доступа.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankOperationService bankOperationService;
    private final BankAccountStatusService bankAccountStatusService;

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
        return bankAccountService.getTransactionsByAccountIdAndPageable(id, pageable);
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
     * @param request DTO с данными для списания средств со счета.
     */
    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@RequestBody @Valid OperationRequestDto request) {
        bankOperationService.withdraw(
                request.getAccountId(),
                request.getAmount(),
                request.getSourceUserId(),
                request.getTargetUserId());
    }

    /**
     * Выполняет пополнение средств для указанного счета.
     *
     * @param request DTO с данными для пополнения средств для счета.
     */
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@RequestBody @Valid OperationRequestDto request) {
        bankOperationService.deposit(
                request.getAccountId(),
                request.getAmount(),
                request.getSourceUserId(),
                request.getTargetUserId());
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
        bankOperationService.transferBetweenAccounts(
                request.getSourceUserId(),
                request.getTargetUserId(),
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
    }
}
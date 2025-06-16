package com.gigabank.controllers;

import com.gigabank.models.dto.request.account.TransferRequestDto;
import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.service.account.BankAccountOperationService;
import com.gigabank.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST контроллер для управления транзакциями.
 * Предоставляет API для создания, управления и получения информации о транзакциях.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountOperationService bankAccountOperationService;

    /**
     * Создает новую транзакцию для указанного счета.
     *
     * @param request DTO с данными для создания транзакции
     * @return DTO созданной транзакции
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDto createAccount(@Valid @RequestBody CreateTransactionRequestDto request) {
        return transactionService.createTransactionFromController(request);
    }

    /**
     * Получает полную информацию о транзакции по его идентификатору.
     *
     * @param id уникальный идентификатор транзакции
     * @return DTO с информацией о транзакции
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponseDto getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    /**
     * Выполняет перевод средств между счетами
     *
     * @param request DTO с данными для перевода (счет отправителя, счет получателя, сумма)
     */
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void transferBetweenAccounts(
            @RequestBody @Valid TransferRequestDto request) {
        bankAccountOperationService.transferBetweenAccounts(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
    }
}
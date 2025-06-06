package com.gigabank.controllers;

import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST контроллер для управления банковскими счетами.
 * Предоставляет API для создания, управления и получения информации о счетах.
 * Все методы требуют аутентификации и соответствующих прав доступа.
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Controller", description = "Контроллер для управления банковскими счетами")
public class AccountController {
    private final AccountService accountService;

    /**
     * Создает новый банковский счет для указанного пользователя.
     *
     * @param request DTO с данными для создания счета
     * @return DTO созданного счета
     * @throws AccountValidationException если данные счета не прошли валидацию
     * @apiNote Требует роли ADMIN или владельца счета
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новый счет",
            description = "Создает новый банковский счет с указанными параметрами")
    public AccountResponseDto createAccount(@Valid @RequestBody CreateAccountRequestDto request) {
        return accountService.createAccount(request);
    }

    /**
     * Получает полную информацию о счете по его идентификатору.
     *
     * @param id уникальный идентификатор счета
     * @return DTO с информацией о счете
     * @throws AccountNotFoundException если счет с указанным ID не найден
     * @apiNote Возвращает 204 NO CONTENT при успешном запросе без тела ответа
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить информацию о счете",
            description = "Возвращает полную информацию о банковском счете по его ID")
    public AccountResponseDto getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    /**
     * Удаляет счет по его идентификатору.
     *
     * @param id уникальный идентификатор счета для удаления
     * @throws AccountNotFoundException   если счет с указанным ID не найден
     * @throws AccountValidationException если ID счета невалиден
     * @apiNote Требует роли ADMIN. При удалении счета все связанные транзакции также удаляются.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удалить счет",
            description = "Полностью удаляет банковский счет по его ID")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteById(id);
    }

    /**
     * Получает список всех транзакций по указанному счету.
     *
     * @param id уникальный идентификатор счета
     * @return список DTO транзакций
     * @throws AccountNotFoundException если счет с указанным ID не найден
     * @apiNote Список отсортирован по дате транзакции (от новых к старым)
     */
    @GetMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить транзакции по счету",
            description = "Возвращает историю всех транзакций по указанному счету")
    public List<TransactionResponseDto> getAccountTransactions(@PathVariable Long id) {
        return accountService.getAccountTransactions(id);
    }

    /**
     * Выполняет списание средств с указанного счета.
     *
     * @param id     уникальный идентификатор счета
     * @param amount сумма для списания (должна быть положительной)
     * @throws AccountNotFoundException   если счет не найден
     * @throws AccountValidationException если сумма невалидна или недостаточно средств
     * @apiNote Изменяет баланс счета атомарно
     */
    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Снять средства со счета",
            description = "Выполняет списание указанной суммы с банковского счета")
    public void withdraw(@PathVariable Long id, @RequestParam @PositiveOrZero BigDecimal amount) {
        accountService.withdraw(id, amount);
    }

    /**
     * Выполняет пополнение указанного счета.
     *
     * @param id     уникальный идентификатор счета
     * @param amount сумма для пополнения (должна быть положительной)
     * @throws AccountNotFoundException   если счет не найден
     * @throws AccountValidationException если сумма невалидна
     * @apiNote Изменяет баланс счета атомарно
     */
    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Пополнить баланс счета",
            description = "Пополняет баланс счета на указанную сумму")
    public void deposit(@PathVariable Long id, @RequestParam @PositiveOrZero BigDecimal amount) {
        accountService.deposit(id, amount);
    }
}
package com.gigabank.controllers;

import com.gigabank.exceptions.AccountException;
import com.gigabank.mappers.AccountMapper;
import com.gigabank.models.dto.AccountDto;
import com.gigabank.models.entity.Account;
import com.gigabank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    /*
//todo СДЕЛАЙ описание контроллера + добавить методы по привязке счета к пользователю!
//todo Сделать три метода (deposit/withdraw/transactions) + добавить описание к классам!!!
- POST /accounts/{id}/deposit — пополнить баланс
- POST /accounts/{id}/withdraw — снять средства
- GET /accounts/{id}/transactions — получить историю операций
 */

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    //- POST /accounts — создать новый аккаунт
    // Создание нового счёта (без привязки к пользователю)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountDto> createAccount() {
        accountService.create();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //- GET /accounts/{id} — получить данные аккаунта
    // Получение счёта по ID
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        Account account = accountService.getById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accountMapper.toDto(account));
    }
    /*
    @GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
}
     */

    // Обновление баланса счёта
    @PatchMapping("/{id}/balance")
    public ResponseEntity<Void> updateBalance(
            @PathVariable Long id,
            @RequestParam BigDecimal balance
    ) throws AccountException {
        Account account = accountService.getById(id);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        accountService.updateBalance(account, balance);
        return ResponseEntity.ok().build();
    }

    // Удаление счёта
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) throws AccountException {
        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Сохранение/обновление счёта (полное обновление данных)
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountDto accountDto
    ) throws AccountException {
        Account existingAccount = accountService.getById(id);
        if (existingAccount == null) {
            return ResponseEntity.notFound().build();
        }

        Account updatedAccount = accountMapper.toEntity(accountDto);
        updatedAccount.setId(existingAccount.getId()); // Сохраняем исходный ID
        accountService.save(updatedAccount);

        return ResponseEntity.ok(accountMapper.toDto(updatedAccount));
    }
}
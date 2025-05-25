package com.gigabank.service;

import com.gigabank.exceptions.AccountException;
import com.gigabank.models.entity.Account;
import com.gigabank.repository.AccountRepository;
import com.gigabank.utility.validators.ValidateAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.gigabank.utility.Utility.isFilled;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ValidateAccountService validateAccountService;

    @Transactional
    public void create() {
        Account account = Account.builder()
                .balance(BigDecimal.ZERO)
                .build();

        log.info("Создан счет с UUID: {}", account.getId());
    }

    public void updateBalance(Account account, BigDecimal balance) {
        validateAccountService.ValidateAccountData(account);
        account.setBalance(balance);
    }

    public Account getById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Account account) throws AccountException {
        validateAccountService.ValidateAccountData(account);
        accountRepository.save(account);
    }

    @Transactional
    public void deleteById(Long id) throws AccountException {
        if (!isFilled(id)) {
            log.error("Попытка удаления счета с пустым id");
            throw new AccountException("Id не может быть пустым");
        }
        if (accountRepository.deleteById(id) == 0) {
            log.warn("Счет с id {} не найден", id);
            throw new AccountException("Счет не найден. id: " + id);
        }
        log.info("Счет с id {} удален", id);
    }
}
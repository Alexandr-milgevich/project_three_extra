package com.gigabank.utility.validators;

import com.gigabank.exceptions.AccountException;
import com.gigabank.models.entity.Account;
import com.gigabank.repository.AccountRepository;
import com.gigabank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateAccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void ValidateAccountData(Account account) throws AccountException {
        checkAccount(account);
        checkUuid(account);
        checkBalance(account);
        checkUser(account);
    }

    private void checkAccount(Account account) throws AccountException {
        if (Objects.isNull(account)) throw new AccountException("Счета не существует.");
    }

    private void checkBalance(Account account) throws AccountException {
        if (Objects.isNull(account.getBalance())) throw new AccountException("Баланс не может быть пустым.");
        if (account.getBalance().signum() < 0) throw new AccountException("Баланс не может быть отрицательным.");
    }

    private void checkUuid(Account account) throws AccountException {
        if (!isFilled(account.getId())) throw new AccountException("UUID не может быть пустым.");
    }

    private void checkUser(Account account) throws AccountException {
        if (Objects.isNull(account.getUser()))
            throw new AccountException("Счет не может быть не привязанным к пользователю");
    }
}
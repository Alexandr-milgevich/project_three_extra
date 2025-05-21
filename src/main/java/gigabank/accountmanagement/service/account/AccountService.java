package gigabank.accountmanagement.service.account;

import gigabank.accountmanagement.exceptions.AccountException;
import gigabank.accountmanagement.models.entity.Account;
import gigabank.accountmanagement.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account create(BigDecimal balance) {

        return Account.builder()
                .uuid(UUID.randomUUID().toString())
                .balance(balance)
                .build();
    }

    public Account getByUuid(String uuid) {
        return accountRepository.findByUuid(uuid).orElse(null);
    }

    public Account save(Account account) throws AccountException {
        if (Objects.isNull(account))
            throw new AccountException("Счета не существует!");
        if (Objects.isNull(account.getUuid()) || account.getUuid().isBlank())
            throw new AccountException("Идентификатор счета отсутствует");
        if (Objects.isNull(account.getBalance()))
            throw new AccountException("Баланс должен быть указан");
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0)
            throw new AccountException("Счет не может быть отрицательным");
        if (Objects.isNull(account.getUser()))
            throw new AccountException("Счет не может быть не привязанным к пользователю");

        return accountRepository.save(account);
    }

    public Account delete(String uuid) throws AccountException {
        if (Objects.isNull(uuid) || uuid.isBlank())
            throw new AccountException("Идентификатор счета отсутствует");
        int deletedRecords = accountRepository.deleteByUuid(uuid);
        if (deletedRecords <= 0)
            throw new AccountException("Счет не найден: " + uuid);
        return accountRepository.findByUuid(uuid).orElse(null);
    }
}
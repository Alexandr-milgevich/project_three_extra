package com.gigabank.service;

import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.mappers.AccountMapper;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.entity.Account;
import com.gigabank.models.entity.User;
import com.gigabank.repository.AccountRepository;
import com.gigabank.utility.validators.ValidateAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с банковскими счетами.
 * Предоставляет методы для создания, управления и получения информации о счетах.
 * Обеспечивает выполнение операций пополнения, списания и перевода средств.
 * Все изменения баланса выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ValidateAccountService validator;
    private final AccountMapper accountMapper;

    /**
     * Создает новый банковский счет на основе переданных данных.
     *
     * @param createDto DTO с данными для создания счета
     * @return DTO созданного счета
     * @throws AccountValidationException если данные счета не прошли валидацию
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto createDto) {
        Account account = accountMapper.toEntity(createDto);
        save(account);
        log.info("Создан счет с id: {}", account.getId());
        return accountMapper.toDto(account);
    }

    /**
     * Создает начальный счет для нового пользователя.
     *
     * @param user пользователь, для которого создается счет
     * @return созданный счет с нулевым балансом
     * @throws AccountValidationException если данные счета не прошли валидацию
     */
    @Transactional
    public Account createInitialAccount(User user) {
        Account account = Account.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .listTransactions(new ArrayList<>())
                .build();

        save(account);
        log.info("Создан первый счет пользователя. Его id: {}", account.getId());
        return account;
    }

    /**
     * Получает информацию о счете по его идентификатору.
     *
     * @param id идентификатор счета
     * @return DTO с информацией о счете
     * @throws AccountNotFoundException если счет с указанным id не найден
     */
    public AccountResponseDto getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toDto)
                .orElseThrow(() -> new AccountNotFoundException("Счет не найден. Его id: " + id));
    }

    /**
     * Сохраняет информацию о счете в базу данных.
     * Перед сохранением выполняет валидацию данных счета.
     *
     * @param account сущность счета для сохранения
     * @throws AccountValidationException если данные счета не прошли валидацию
     */
    @Transactional
    public void save(Account account) {
        validator.validate(account);
        accountRepository.save(account);
        log.info("Счет был сохранен в БД. Id: {} ", account.getId());
    }

    /**
     * Удаляет счет по указанному идентификатору.
     *
     * @param id идентификатор счета для удаления
     * @throws AccountValidationException если id равен null
     * @throws AccountNotFoundException   если счет с указанным id не найден
     */
    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            log.error("Попытка удаления счета с пустым id");
            throw new AccountValidationException("Id не может быть пустым");
        }
        if (accountRepository.deleteById(id) == 0) {
            log.warn("Счет не найден. Его id: {}", id);
            throw new AccountNotFoundException("Счет не найден. id: " + id);
        }
        log.info("Счет с id {} удален", id);
    }

    /**
     * Получает список транзакций по указанному счету.
     *
     * @param id идентификатор счета
     * @return список DTO транзакций
     * @throws AccountNotFoundException если счет с указанным id не найден
     */
    public List<TransactionResponseDto> getAccountTransactions(Long id) {
        return getAccountById(id).getListTransactionResponseDto();
    }

    /**
     * Выполняет списание средств с указанного счета.
     *
     * @param id     идентификатор счета для списания
     * @param amount сумма для списания
     * @throws AccountValidationException если сумма отрицательная или недостаточно средств
     * @throws AccountNotFoundException   если счет не найден
     */
    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        Account account = validator.validateForOperation(id, amount);
        if (account.getBalance().compareTo(amount) < 0)
            throw new AccountValidationException("Недостаточно средств на счете для списания");
        account.setBalance(account.getBalance().subtract(amount));
        save(account);

        log.info("Произведено списание со счета на сумму: {}. Id: {}", amount, account.getId());
    }

    /**
     * Выполняет пополнение указанного счета.
     *
     * @param id     идентификатор счета для пополнения
     * @param amount сумма для пополнения
     * @throws AccountValidationException если сумма отрицательная
     * @throws AccountNotFoundException   если счет не найден
     */
    @Transactional
    public void deposit(Long id, BigDecimal amount) {
        Account account = validator.validateForOperation(id, amount);
        account.setBalance(account.getBalance().add(amount));
        save(account);

        log.info("Произведено пополнение со счета на сумму: {}. Id: {}", amount, account.getId());
    }
}
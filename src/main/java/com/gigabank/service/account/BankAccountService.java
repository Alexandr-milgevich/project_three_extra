package com.gigabank.service.account;

import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.mappers.AccountMapper;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.repository.AccountRepository;
import com.gigabank.utility.validators.ValidateAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для работы с банковскими счетами.
 * Предоставляет методы для создания, управления и получения информации о счетах.
 * Обеспечивает выполнение операций пополнения, списания и перевода средств.
 * Все изменения баланса выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final ValidateAccountService validateAccountService;

    /**
     * Создает новый банковский счет на основе переданных данных.
     *
     * @param createDto DTO с данными для создания счета
     * @return DTO созданного счета
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto createDto) {
        log.info("Начало создания счета: {}", createDto);

        BankAccount bankAccount = accountMapper.toEntityFromCreateRequestDto(createDto);
        save(bankAccount);

        log.info("Создан счет с id: {}", bankAccount.getId());
        return accountMapper.toResponseDto(bankAccount);
    }

    /**
     * Создает начальный счет для нового пользователя.
     *
     * @param user пользователь, для которого создается счет
     * @return созданный счет с нулевым балансом
     */
    @Transactional
    public BankAccount createInitialAccount(User user) {
        log.info("Начало создания default счета пользователя:  {}", user);

        BankAccount bankAccount = BankAccount.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        save(bankAccount);

        log.info("Создан первый счет пользователя. Его id: {}", bankAccount.getId());
        return bankAccount;
    }

    /**
     * Получает информацию о счете по его идентификатору.
     *
     * @param id идентификатор счета
     * @return DTO с информацией о счете
     */
    public AccountResponseDto getAccountById(Long id) {
        log.info("Начало поиска счета по id: {}", id);

        AccountResponseDto dto = accountRepository.findById(id)
                .map(accountMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(BankAccount.class, id));

        log.info("Счет найден. id: {}", id);
        return dto;
    }

    /**
     * Получает информацию о счете по его идентификатору.
     *
     * @param id идентификатор счета
     * @return DTO с информацией о счете
     */
    public BankAccount getAccountEntityById(Long id) {
        log.info("Начало поиска счета с id: {}", id);

        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BankAccount.class, id));

        log.info("Счет найден. Его id: {}", id);
        return account;
    }

    /**
     * Сохраняет информацию о счете в базу данных.
     * Перед сохранением выполняет валидацию данных счета.
     *
     * @param bankAccount сущность счета для сохранения
     */
    public void save(BankAccount bankAccount) {
        log.debug("Начало сохранения счета: {}", bankAccount);

        validateAccountService.validateUnderSave(bankAccount);
        accountRepository.save(bankAccount);

        log.info("Счет был сохранен в БД. Id: {} ", bankAccount.getId());
    }

    /**
     * Удаление счета по идентификатору
     *
     * @param id идентификатор счета
     */
    @Transactional
    public void deleteBankAccountById(Long id) {
        log.info("Попытка удаления счета по id: {}", id);

        if (id != null) {
            int deletedCount = accountRepository.deleteAccountById(id);
            if (deletedCount == 0)
                throw new EntityNotFoundException(BankAccount.class, "Счет не найден: ");
        }

        log.info("Удален счет");
    }
}
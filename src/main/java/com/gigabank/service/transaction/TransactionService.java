package com.gigabank.service.transaction;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.mappers.TransactionMapper;
import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.TransactionRepository;
import com.gigabank.service.account.AccountService;
import com.gigabank.service.account.BankOperationService;
import com.gigabank.utility.validators.ValidateTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис отвечает за управление платежами и переводами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidateTransactionService validateTransactionService;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final BankOperationService bankOperationService;

    /**
     * Создает новую транзакцию на основе переданных данных.
     *
     * @param createDto DTO с данными для создания транзакции
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransactionFromController(CreateTransactionRequestDto createDto) {
        log.info("Попытка создания транзакции: {}", createDto);

        Transaction transaction = transactionMapper.toEntityFromCreateRequestDto(createDto);
        save(transaction);

        log.info("Создана транзакция с id: {}", transaction.getId());
        return transactionMapper.toResponseDto(transaction);
    }

    /**
     * Создает новую транзакцию на основе переданных данных.
     *
     * @param amount -
     * @param type   -
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransaction(BigDecimal amount, String type,
                                                    String categories, Long accountId) {
        log.info("Попытка создания транзакции.");

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(type)
                .category(categories)
                .createdDate(LocalDateTime.now())
                .status(TransactionStatus.ACTIVE)
                .bankAccount(accountService.getAccountById(accountId))
                .build();

        save(transaction);

        log.info("Создана транзакция с uuid: {}", transaction.getTransactionUuid());

        return transactionMapper.toResponseDto(transaction);
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     */
    public TransactionResponseDto getTransactionByIdFromController(long id) {
        log.info("Попытка получить транзакцию по ID: {}", id);

        TransactionResponseDto dto = transactionRepository.findByIdAndStatus(id, TransactionStatus.ACTIVE)
                .map(transactionMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию с ID: {}", id);
        return dto;
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     */
    public Transaction getTransactionById(long id) {
        log.info("Попытка получить транзакцию c ID: {}", id);

        Transaction transaction = transactionRepository.findByIdAndStatus(id, TransactionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию по ID: {}", id);
        return transaction;
    }

    public Page<TransactionResponseDto> getByBankAccountAndPageable(BankAccount account, Pageable pageable) {
        log.info("Попытка получить транзакцию по счету с пагинацией по ID: {}", account.getId());

        Page<TransactionResponseDto> responseDtoPage = transactionRepository.findByBankAccount(account, pageable)
                .map(transactionMapper::toResponseDto);

        log.info("Получены транзакции по счету с пагинацией по ID: {}", account.getId());
        return responseDtoPage;
    }

    /**
     * Сохраняет информацию о транзакции в базу данных.
     * Перед сохранением выполняет валидацию данных транзакции.
     *
     * @param transaction сущность транзакции для сохранения
     */
    public void save(Transaction transaction) {
        log.debug("Начало сохранения транзакции: {}", transaction);

        validateTransactionService.validateUnderSave(transaction);
        transactionRepository.save(transaction);

        log.info("Транзакция была сохранена в БД. Id: {} ", transaction.getId());
    }

    /**
     * Выполняет перевод средств между счетами
     *
     * @param fromAccountId ID счета отправителя
     * @param toAccountId   ID счета получателя
     * @param amount        сумма перевода
     */
    @Transactional
    public void transferBetweenAccounts(Long fromAccountId,
                                        Long toAccountId, BigDecimal amount) {
        log.info("Начало перевода {} со счета {} на счет {}", amount, fromAccountId, toAccountId);

        if (fromAccountId.equals(toAccountId))
            throw new EntityValidationException(Transaction.class, "Нельзя переводить на тот же счет");
        bankOperationService.withdraw(fromAccountId, amount);
        bankOperationService.deposit(toAccountId, amount);

        log.info("Успешный перевод {} со счета {} на счет {}", amount, fromAccountId, toAccountId);
    }

    /**
     * Изменяет статус счета при изменении состояния пользователя (BLOCKED, DELETED).
     *
     * @param transactions  список транзакций счетов
     * @param accountStatus новый статус счета
     */
    @Transactional
    public void updateTransactionsStatus(List<Transaction> transactions, AccountStatus accountStatus) {
        log.info("Начало изменений статуса для списка транзакций. Статус счета: {}", accountStatus);

        TransactionStatus transactionStatus = switch (accountStatus) {
            case ACTIVE -> TransactionStatus.ACTIVE;
            case BLOCKED -> TransactionStatus.BLOCKED;
            case ARCHIVED -> TransactionStatus.ARCHIVED;
        };

        transactions.forEach(transaction -> {
            transaction.setStatus(transactionStatus);
            transactionRepository.save(transaction);
        });

        log.info("Изменился статус для списка транзакций. Статус счета: {}", accountStatus);
    }

    /**
     * Изменяет статус транзакции по идентификатору.
     *
     * @param id        идентификатор транзакции
     * @param newStatus новый статус транзакции
     */
    @Transactional
    public void changeTransactionStatus(Long id, TransactionStatus newStatus) {
        log.info("Попытка изменения статуса транзакции на {} с ID: {}", newStatus, id);

        Transaction transaction = getTransactionById(id);
        TransactionStatus oldStatus = transaction.getStatus();
        validateTransactionService.checkTransactionStatus(newStatus, oldStatus);
        transaction.setStatus(newStatus);
        save(transaction);

        log.info("Статус транзакции изменен c {} на {} с ID: {}", oldStatus, newStatus, id);
    }
}
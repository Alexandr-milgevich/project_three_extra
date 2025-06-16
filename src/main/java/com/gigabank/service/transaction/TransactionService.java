package com.gigabank.service.transaction;

import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.mappers.TransactionMapper;
import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.TransactionRepository;
import com.gigabank.utility.validators.ValidateTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сервис отвечает за управление платежами и переводами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final ValidateTransactionService validateTransactionService;

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
     * @param amount - сумма изменения баланса
     * @param type   - тип транзакции
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransaction(BigDecimal amount, String type, BankAccount bankAccount) {
        log.info("Попытка создания транзакции.");

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(type)
                .bankAccount(bankAccount)
                .createdDate(LocalDateTime.now())
                .build();

        save(transaction);

        log.info("Создана транзакция.");
        return transactionMapper.toResponseDto(transaction);
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     */
    public TransactionResponseDto getTransactionById(long id) {
        log.info("Попытка получить транзакцию по ID: {}", id);

        TransactionResponseDto dto = transactionRepository.findById(id)
                .map(transactionMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию с ID: {}", id);
        return dto;
    }

    /**
     * Получает список транзакций по указанному счету.
     *
     * @param accountId идентификатор счета
     * @return список DTO транзакций
     */
    public Page<TransactionResponseDto> getTransactionsByAccountIdAndPageable(Long accountId, Pageable pageable) {
        log.info("Получение транзакций по счету {} с пагинацией {}", accountId, pageable);

        Page<TransactionResponseDto> transactionsPage = transactionRepository
                .findByBankAccountId(accountId, pageable)
                .map(transactionMapper::toResponseDto);

        log.info("Получены транзакции по счету с пагинацией по ID: {}", accountId);
        return transactionsPage;
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

    public void deleteTransactionById(Long id) {
        log.info("Попытка удаления транзакции по id: {}", id);

        if (id != null) {
            int deletedCount = transactionRepository.deleteTransactionById(id);
            if (deletedCount == 0)
                throw new EntityNotFoundException(Transaction.class, "Транзакция не найдена: ");
        }

        log.info("Удалена транзакция");
    }
}
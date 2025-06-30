package com.gigabank.service.audit;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.entity.UserStatusAudit;
import com.gigabank.repository.UserStatusAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для аудита изменений статусов пользователей.
 * Обеспечивает запись в лог изменений статусов пользователей для последующего аудита и анализа.
 * Все операции выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    private final UserStatusAuditRepository auditRepository;

    /**
     * Логирует изменение статуса пользователя в системе аудита.
     * Метод выполняется в транзакции для гарантии сохранения записи аудита.
     *
     * @param userId идентификатор пользователя, для которого изменяется статус
     * @param oldStatus предыдущий статус пользователя
     * @param newStatus новый статус пользователя
     */
    @Transactional
    public void logStatusChange(Long userId, UserStatus oldStatus,
                                UserStatus newStatus) {
        log.info("Попытка добавить запись аудита в БД. Изменяемый Id: {}", userId);

        UserStatusAudit audit = createUserStatusAudit(userId, oldStatus, newStatus);
        auditRepository.save(audit);

        log.info("Аудит: Пользователю ({}) изменили статус с {} на {}.", userId, oldStatus, newStatus);
    }

    /**
     * Создает объект аудита для изменения статуса пользователя.
     * Вспомогательный метод для инкапсуляции логики создания объекта аудита.
     *
     * @param userId идентификатор пользователя
     * @param oldStatus предыдущий статус пользователя
     * @param newStatus новый статус пользователя
     * @return созданный объект {@link UserStatusAudit}
     */
    private UserStatusAudit createUserStatusAudit(Long userId, UserStatus oldStatus,
                                                  UserStatus newStatus) {
        return UserStatusAudit.builder()
                .id(userId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .build();
    }
}
package com.gigabank.service;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.entity.UserStatusAudit;
import com.gigabank.repository.UserStatusAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {
    private final UserStatusAuditRepository auditRepository;

    @Transactional
    public void logStatusChange(Long userId, UserStatus oldStatus,
                                UserStatus newStatus, String reason) {
        log.info("Попытка добавить запись аудита в БД. Изменяемый Id: {}", userId);

        UserStatusAudit audit = createUserStatusAudit(userId, oldStatus, newStatus, reason);
        auditRepository.save(audit);

        log.info("Аудит: Пользователю ({}) изменили статус с {} на {}. Причина изменения: {}",
                userId, oldStatus, newStatus, reason);
    }

    private UserStatusAudit createUserStatusAudit(Long userId, UserStatus oldStatus,
                                                  UserStatus newStatus, String reason) {
        return UserStatusAudit.builder()
                .userId(userId)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .reason(reason)
                .build();
    }
}
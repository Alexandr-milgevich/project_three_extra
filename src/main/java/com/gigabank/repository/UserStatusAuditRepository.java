package com.gigabank.repository;

import com.gigabank.models.entity.UserStatusAudit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы логирования операций,
 * связанных с изменением статуса пользователя.
 * Обеспечивает доступ к данным пользователя в базе данных.
 */
public interface UserStatusAuditRepository extends JpaRepository<UserStatusAudit, Long> {
}
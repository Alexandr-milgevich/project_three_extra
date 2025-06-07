package com.gigabank.models.entity;

import com.gigabank.constants.status.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность аудита изменений статуса пользователя.
 * Хранит информацию об изменениях статуса пользователя.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_status_audit")
public class UserStatusAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus newStatus;

    private String reason; // Причина изменения

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime changedAt;
}
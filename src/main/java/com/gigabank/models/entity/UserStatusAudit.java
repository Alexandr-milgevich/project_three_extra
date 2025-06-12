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
    @Column(name = "audit_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", nullable = false)
    private UserStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private UserStatus newStatus;

    @Column(name = "changed_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime changedAt;

    @Column(name = "reason")
    private String reason; // Причина изменения

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
}
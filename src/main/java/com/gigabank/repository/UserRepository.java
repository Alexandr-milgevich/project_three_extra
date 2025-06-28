package com.gigabank.repository;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователем.
 * Обеспечивает доступ к данным пользователя в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(Long id);

    Optional<User> findByIdAndStatus(Long id, UserStatus status);

    Optional<List<User>> findAllBy();

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsById(Long id);
}
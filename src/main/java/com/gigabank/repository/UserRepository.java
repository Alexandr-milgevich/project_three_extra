package com.gigabank.repository;

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
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<List<User>> findAllBy();

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsById(Long id);

    int deleteUserById(Long id);
}
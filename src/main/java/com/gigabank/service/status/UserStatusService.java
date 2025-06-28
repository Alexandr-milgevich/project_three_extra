package com.gigabank.service.status;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import com.gigabank.service.AuditService;
import com.gigabank.service.UserService;
import com.gigabank.utility.validators.ValidateUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис по изменению статуса пользователя
 * и каскадного обновления его зависимостей (BankAccount, Transaction)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatusService implements StatusChanger<User, UserStatus> {

    private final UserService userService;
    private final AuditService auditService;
    private final ValidateUserService validateUserService;
    private final BankAccountStatusService bankAccountStatusService;
    private final UserRepository userRepository;

    /**
     * Изменяет статус пользователя по идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param newStatus новый статус пользователя
     */
    @Override
    @Transactional
    public void changeStatus(Long id, UserStatus newStatus) {
        log.info("Попытка изменения статуса пользователя на {} с ID: {}", newStatus, id);

        User user = userService.getUserById(id);
        UserStatus oldStatus = user.getStatus();
        validateUserService.checkUserStatus(newStatus, oldStatus);
        user.setStatus(newStatus);
        validateUserService.validateUserBeforeSave(user);
        bankAccountStatusService.updateStatus(user.getListBankAccounts(), newStatus);
        userRepository.save(user);

        auditService.logStatusChange(id, oldStatus, newStatus);
        log.info("Статус пользователя изменен c {} на {} с ID: {}", oldStatus, newStatus, id);
    }
}

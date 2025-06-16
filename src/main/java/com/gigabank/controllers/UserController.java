package com.gigabank.controllers;

import com.gigabank.models.dto.request.user.ChangeStatusUserRequest;
import com.gigabank.models.dto.request.user.CreateUserRequestDto;
import com.gigabank.models.dto.request.user.UpdateUserRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с пользователями банка.
 * Предоставляет REST API для создания, получения, обновления и удаления
 * пользователей на основе их идентификаторов или email.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param createDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto createDto) {
        return userService.createUser(createDto);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserByIdFromController(id);
    }

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param updateDto DTO с новыми данными для обновления
     * @return DTO обновленного пользователя
     */
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UpdateUserRequestDto updateDto) {
        return userService.updateUser(id, updateDto);
    }

    /**
     * Изменяет статус пользователя по идентификатору.
     * Также изменяются каскадно статусы его счетов
     * и транзакций на соответствующий.
     *
     * @param id      идентификатор пользователя
     * @param request DTO с новым статусом и причиной изменения
     */
    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeUserStatus(@PathVariable Long id,
                                 @Valid @RequestBody ChangeStatusUserRequest request) {
        userService.changeUserStatus(id, request.getStatus(), request.getReason());
    }
}
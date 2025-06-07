package com.gigabank.controllers;

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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param createDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    @PostMapping("/new")
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
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param updateDto DTO с новыми данными для обновления
     * @return DTO обновленного пользователя
     */
    @PutMapping("/update/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UpdateUserRequestDto updateDto) {
        return userService.updateUser(id, updateDto);
    }

    //todo Подумать про каскадное удаление. Идея следующая:
    // при удалении счета\пользователя у всех изменять статус, а не удалять.

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @DeleteMapping("/delete/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
package com.gigabank.controllers;

import com.gigabank.exceptions.User.UserAlreadyExistsException;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.User.UserValidationException;
import com.gigabank.models.dto.request.user.UserCreateRequestDto;
import com.gigabank.models.dto.request.user.UserUpdateRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Controller", description = "Контроллер для управления пользователями")
public class UserController {
    private final UserService userService;

    /**
     * Регистрирует нового пользователя.
     *
     * @param requestDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     * @throws UserValidationException    если данные пользователя не прошли валидацию
     * @throws UserAlreadyExistsException если пользователь с таким email или телефоном уже существует
     */
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserCreateRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь с данным id не найден
     */
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь с данным email не найден
     */
    @GetMapping("/email/{email}")
    public UserResponseDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Обновляет данные пользователя по его идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param updateDto DTO с новыми данными для обновления
     * @return DTO обновленного пользователя
     * @throws UserNotFoundException   если пользователь с данным id не найден
     * @throws UserValidationException если данные пользователя не прошли валидацию
     */
    @PutMapping("/update/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserUpdateRequestDto updateDto) {
        return userService.updateUser(id, updateDto);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserNotFoundException если пользователь с данным id не найден
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    /**
     * Удаляет пользователя по email.
     *
     * @param email email пользователя
     * @throws UserNotFoundException если пользователь с данным email не найден
     */
    @DeleteMapping("/delete/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByEmail(@PathVariable String email) {
        userService.deleteUserByEmail(email);
    }
}
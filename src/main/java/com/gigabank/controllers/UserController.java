package com.gigabank.controllers;

import com.gigabank.exceptions.UserException;
import com.gigabank.mappers.UserMapper;
import com.gigabank.models.dto.UserDto;
import com.gigabank.models.entity.User;
import com.gigabank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        try {
            userService.create(
                    user.getEmail(),
                    user.getLastName(),
                    user.getFirstName(),
                    user.getMiddleName(),
                    user.getPhoneNumber(),
                    user.getBirthDate()
            );
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User user = userService.getByEmail(email);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        User updatedUser = userMapper.toEntity(userDto);
        updatedUser.setId(existingUser.getId());
        try {
            userService.save(updatedUser);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteById(id);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String email) {
        try {
            userService.deleteByEmail(email);
        } catch (UserException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }
}
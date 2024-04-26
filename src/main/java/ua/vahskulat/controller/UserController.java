package ua.vahskulat.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.vahskulat.dto.request.UserCreateRequestDTO;
import ua.vahskulat.dto.request.UserUpdateRequestDto;
import ua.vahskulat.dto.response.UserResponseAPI;
import ua.vahskulat.dto.response.UserResponseDto;
import ua.vahskulat.mapper.UserMapper;
import ua.vahskulat.model.User;
import ua.vahskulat.service.UserService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.mapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseAPI<UserResponseDto>> userById(@Positive @PathVariable Long id
            , HttpServletRequest request) {
        log.info("Received request to get user with ID: {}", id);

        return ResponseEntity
                .ok()
                .body(UserResponseAPI.<UserResponseDto>builder()
                        .data(userMapper.toDTO(userService.getUserById(id)))
                        .path(request.getRequestURI())
                        .build());

    }

    @PostMapping
    public ResponseEntity<UserResponseAPI<UserResponseDto>> createUser(
            @Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO
            , HttpServletRequest request) {
        log.info(" Received request to create user: {} ", userCreateRequestDTO);
        User user = userService.createUser(userMapper.toUser(userCreateRequestDTO));
        log.info(" User created successfully ");

        return ResponseEntity.
                created(URI.create(request.getRequestURI() + "/" + user.getId()))
                .body(UserResponseAPI.<UserResponseDto>builder()
                        .data(userMapper.toDTO(user))
                        .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseAPI<UserResponseDto>> updateUserNames
            (@PathVariable @Positive Long id, @Valid @RequestBody UserUpdateRequestDto updateUser
                    , HttpServletRequest request) {
        log.info(" Received request to update user first name: {} and last name : {} with ID: {}"
                , updateUser.firstName(), updateUser.lastName(), id);
        User user = userService.updateUserNames(id, updateUser.firstName(), updateUser.lastName());
        log.info(" User first name and last name updated successfully ");

        return ResponseEntity
                .ok()
                .body(UserResponseAPI.<UserResponseDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(request.getRequestURI())
                        .build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseAPI<UserResponseDto>> updateAllFieldsUser(@PathVariable @Positive Long id
            , @Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO
            , HttpServletRequest request) {
        log.info(" Received request to update user with id: {}", id);
        User user = userService.updateUser(id, userMapper.toUser(userCreateRequestDTO));
        log.info(" User updated successfully ");

        return ResponseEntity.ok()
                .body(UserResponseAPI.<UserResponseDto>builder()
                        .data(userMapper.toDTO(user))
                        .path(request.getRequestURI())
                        .build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long id) {
        log.info(" Received request to delete user with id: {}", id);
        userService.deleteUser(id);
        log.info(" User deleted successfully ");

        return ResponseEntity
                .noContent()
                .build();

    }

    @GetMapping()
    public ResponseEntity<UserResponseAPI<List<UserResponseDto>>> findUsersByDateRange(
            @NotNull(message = " From date can`t be null or empty. This date is required ")
            LocalDate fromDate
            , @NotNull(message = "To date can`t be null or empty. This date is required ")
            LocalDate toDate
            , @RequestParam(name = "page", defaultValue = "0", required = false) int page
            , @RequestParam(name = "size", defaultValue = "10", required = false) int size
            , HttpServletRequest request) {
        log.info(" Received request to find all user by date range from: {} - to: {} ", fromDate, toDate);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getUsersByBirthDateRange(fromDate, toDate, pageable);

        return ResponseEntity
                .ok(UserResponseAPI.<List<UserResponseDto>>builder()
                        .data(userMapper.toListDTO(userPage.getContent()))
                        .countElements(userPage.getNumberOfElements())
                        .totalElements(userPage.getTotalElements())
                        .path(request.getRequestURI())
                        .build());
    }
}

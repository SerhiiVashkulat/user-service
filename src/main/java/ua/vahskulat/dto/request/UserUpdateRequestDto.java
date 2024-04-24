package ua.vahskulat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDto(@NotBlank(message = " First name can`t be empty ") String firstName
        , @NotBlank(message = " Last name can`t be empty ") String lastName) {
}

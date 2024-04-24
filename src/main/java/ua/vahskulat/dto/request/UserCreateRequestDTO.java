package ua.vahskulat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ua.vahskulat.model.Address;

import java.time.LocalDate;

public record UserCreateRequestDTO(@NotBlank(message = "Email can't be empty")
                                    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",
                                            message = "Input correct email address")
                                    String email,

                                   @NotBlank(message = "First name can't be empty")
                                    String firstName,

                                   @NotBlank(message = "Last name can't be empty")
                                    String lastName,

                                   @NotNull(message = "Birth day can't be empty")
                                   LocalDate birthDate,

                                   Address address,

                                   String phoneNumber) {
}

package ua.vahskulat.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ua.vahskulat.model.Address;

import java.time.LocalDate;

public record UserResponseDto(String email, String firstName,  String lastName, LocalDate birthDate, Address address,String phoneNumber) {

}

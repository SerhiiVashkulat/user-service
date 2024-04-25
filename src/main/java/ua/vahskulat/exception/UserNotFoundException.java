package ua.vahskulat.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserNotFoundException extends RuntimeException {
    private final String message;
}

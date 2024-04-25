package ua.vahskulat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserWrongDateException extends RuntimeException {
    private final String message;
}

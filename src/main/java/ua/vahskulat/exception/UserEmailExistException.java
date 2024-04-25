package ua.vahskulat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserEmailExistException extends RuntimeException {
  private final String message;
}

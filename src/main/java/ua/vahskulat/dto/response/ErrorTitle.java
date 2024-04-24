package ua.vahskulat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorTitle {

    USER_NOT_FOUND(" User not found "),
    WRONG_DATE(" Wrong date "),
    WRONG_AGE(" Wrong age "),
    USER_EMAIL_EXIST(" User email exist "),
    INVALID_QUERY_PARAMETER("Invalid query parameter"),
    VALIDATION_ERROR(" Validation error ");

    private final String error;

}

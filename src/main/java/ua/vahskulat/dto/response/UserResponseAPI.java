package ua.vahskulat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class UserResponseAPI<T> {
    private T data;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private String path;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long totalElements;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long countElements;

}

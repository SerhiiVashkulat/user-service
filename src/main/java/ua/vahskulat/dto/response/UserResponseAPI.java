package ua.vahskulat.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseAPI<T> {

    private T data;

    private String path;

    private long totalElements;
}

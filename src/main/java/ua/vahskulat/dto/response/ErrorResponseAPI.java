package ua.vahskulat.dto.response;

import java.time.LocalDateTime;
public record ErrorResponseAPI(String error, String detail, String path, LocalDateTime timestamp ){}




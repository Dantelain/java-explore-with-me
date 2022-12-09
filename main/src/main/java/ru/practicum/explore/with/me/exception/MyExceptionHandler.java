package ru.practicum.explore.with.me.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> validationException(ValidationException e) {
        return getResponseEntity(e, "Ошибка ValidationException", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> notFoundException(NotFoundException e) {
        return getResponseEntity(e, "Ошибка NotFoundException", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleException(ConflictException e) {
        return getResponseEntity(e, "Конфликт дубликатов данных", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getResponseEntity(e, "Ошибка MethodArgumentNotValidException", HttpStatus.BAD_REQUEST);
    }

    private <T extends Throwable> ResponseEntity<?> getResponseEntity(T e, String textError, HttpStatus status) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        SampleError sampleError = SampleError.builder()
                .status(status.toString())
                .message(e.getMessage())
                .errors(Arrays.stream(errors.toString().split("\\n\\t")).collect(Collectors.toList()))
                .reason(textError)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(sampleError);
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class SampleError {

        private List<String> errors;
        private String message;
        private String reason;
        private String status;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;

    }
}

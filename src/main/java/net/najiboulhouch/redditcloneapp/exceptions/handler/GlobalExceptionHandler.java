package net.najiboulhouch.redditcloneapp.exceptions.handler;

import net.najiboulhouch.redditcloneapp.exceptions.ErrorResponse;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceFoundException;
import net.najiboulhouch.redditcloneapp.exceptions.ResourceNotFoundException;
import net.najiboulhouch.redditcloneapp.exceptions.SpringRedditException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleFoundException(ResourceFoundException exception){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getCanonicalName())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException exception){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(errorResponse);
    }

    @ExceptionHandler(SpringRedditException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(SpringRedditException exception){
        ErrorResponse errorResponse= ErrorResponse.builder()
                .message(exception.getMessage())
                .exceptionType(exception.getClass().getSimpleName())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }

}

package se.smelly.community.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CouldNotBeFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCouldNotBeFoundException(CouldNotBeFoundException e){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", HttpStatus.NOT_FOUND.name());
        errorBody.put("code", HttpStatus.NOT_FOUND.value());
        errorBody.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }


}

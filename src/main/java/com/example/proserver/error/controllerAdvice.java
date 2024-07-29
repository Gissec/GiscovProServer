package com.example.proserver.error;

import com.example.proserver.DTOs.response.CustomSuccessResponse;
import com.example.proserver.constans.ServerErrorCodes;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@ControllerAdvice
public class controllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<CustomSuccessResponse> handle(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
                new CustomSuccessResponse(ServerErrorCodes.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode(),
                        List.of(ServerErrorCodes.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode())));
    }

    @ExceptionHandler(CustomException.class)
    ResponseEntity<CustomSuccessResponse> handle(CustomException e){
        Integer code = e.getError().getCode();
        return ResponseEntity.badRequest().body(new CustomSuccessResponse<>(code,List.of(code)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomSuccessResponse> handle(MethodArgumentNotValidException e){
        List<Integer> a = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> ServerErrorCodes.mapError.get(objectError.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(a.get(0), a));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<CustomSuccessResponse> handle(ConstraintViolationException e){
        List<Integer> a = e.getConstraintViolations().stream()
                .map(objectError -> ServerErrorCodes.mapError.get(objectError.getMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(a.get(0), a));
    }
}

package com.example.proserver.error;

import com.example.proserver.DTOs.response.BaseSuccessResponse;
import com.example.proserver.DTOs.response.CustomSuccessResponse;
import com.example.proserver.constans.ServerErrorCodes;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
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

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<BaseSuccessResponse> handleMissingPathVariableException(MissingPathVariableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseSuccessResponse(ServerErrorCodes.UNKNOWN.getCode()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseSuccessResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new BaseSuccessResponse(ServerErrorCodes.UNKNOWN.getCode()));
    }
}

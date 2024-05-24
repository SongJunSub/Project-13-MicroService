package org.msa.itemservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.msa.itemservice.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> Exception(HttpServletRequest request, Exception e) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        responseDTOBuilder.code("500").message(e.getMessage());

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) throws Exception {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();

        for(FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField());
            stringBuilder.append(" : ");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append("/ 입력된 값 : ");
            stringBuilder.append(fieldError.getRejectedValue());
        }

        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        responseDTOBuilder.code("500").message(stringBuilder.toString());

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> ApiException(HttpServletRequest request, ApiException e) throws Exception {
        ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

        responseDTOBuilder.code("501").message(e.getMessage());

        return ResponseEntity.ok(responseDTOBuilder.build());
    }

}
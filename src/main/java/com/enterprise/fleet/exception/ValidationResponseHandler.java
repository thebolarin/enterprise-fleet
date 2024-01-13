package com.enterprise.fleet.exception;

import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.util.ValidationError;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ValidationResponseHandler {
    private final BindingResult bindingResult;

    public ResponseEntity<CustomResponse> handleValidationErrors() {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<ValidationError> validationErrors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(
                new CustomResponse(false, "Invalid parameter(s) passed", validationErrors)
        );
    }
}

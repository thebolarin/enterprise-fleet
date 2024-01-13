package com.enterprise.fleet.transaction;

import com.enterprise.fleet.transaction.dto.CreateTransactionDTO;
import com.enterprise.fleet.transaction.dto.UpdateTransactionDTO;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService locationService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> fetchTransactions() {
        return locationService.fetchTransactions();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchTransaction(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return locationService.fetchTransaction(id);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createTransaction (
            @Valid @RequestBody CreateTransactionDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return locationService.createTransaction(request);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateTransaction (
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,

            @Valid @RequestBody UpdateTransactionDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return locationService.updateTransaction(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> deleteTransaction(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return locationService.deleteTransaction(id);
    }
}

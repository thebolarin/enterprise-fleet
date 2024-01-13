package com.enterprise.fleet.invoice;

import com.enterprise.fleet.invoice.dto.*;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoice")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> fetchInvoices() {
        return invoiceService.fetchInvoices();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchInvoice(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return invoiceService.fetchInvoice(id);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createInvoice (
            @Valid @RequestBody CreateInvoiceDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return invoiceService.createInvoice(request);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateInvoice(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,

            @Valid @RequestBody UpdateInvoiceDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return invoiceService.updateInvoice(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> deleteInvoice(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return invoiceService.deleteInvoice(id);
    }
}


package com.enterprise.fleet.invoice.dto;

import com.enterprise.fleet.vehicle_rental.types.RentalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInvoiceDTO {
    @NotBlank(message = "Vehicle Rental cannot be blank")
    private Integer vehicleRentalId;

    @NotNull(message = "Rental amount cannot be blank")
    private BigDecimal amount;

    @NotBlank(message = "Invoice Due Date cannot be blank")
    private LocalDate dueDate;
}


package com.enterprise.fleet.transaction.dto;

import com.enterprise.fleet.transaction.type.TransactionStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDTO {
    @NotNull(message = "Vehicle Rental cannot be blank")
    private Integer vehicleRentalId;

    @NotNull(message = "Rental amount cannot be blank")
    private BigDecimal amount;

    private TransactionStatusType paymentStatus;
}

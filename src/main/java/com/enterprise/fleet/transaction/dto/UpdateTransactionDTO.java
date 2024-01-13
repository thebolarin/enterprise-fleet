package com.enterprise.fleet.transaction.dto;

import com.enterprise.fleet.transaction.type.TransactionStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTransactionDTO {
    private Integer vehicleRentalId;
    private BigDecimal amount;
    private TransactionStatusType paymentStatus;
}

package com.enterprise.fleet.transaction.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionStatusType {
    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    PARTIAL("Partial Payment"),
    CANCELLED("Cancelled"),
    ;

    private final String TransactionStatusType;
}

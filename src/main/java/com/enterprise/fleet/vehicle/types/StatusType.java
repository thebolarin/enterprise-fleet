package com.enterprise.fleet.vehicle.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum StatusType {
    AVAILABLE("available"),
    RENTED("rented"),
    RESERVED("reserved"),
    MAINTENANCE("maintenance"),
    OUT_OF_SERVICE("out:of:service"),
    IN_TRANSIT("in:transit"),
    BLOCKED("blocked"),
    DAMAGED("damaged"),
    PENDING_INSPECTION("pending:inspection"),
    LOST("lost"),
    PENDING_DECOMMISSION("pendign:decommission"),
    RESERVED_FOR_MAINTENANCE("reserved:for:maintence")
    ;

    private final String status;
}

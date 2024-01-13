package com.enterprise.fleet.vehicle_rental.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RentalStatusType {
    REQUESTED("requested"),
    RESERVED("reserved"),
    ACTIVE("active"),
    RETURNED("returned"),
    CANCELLED("cancelled"),
    MAINTENANCE("maintenance"),
    OUT_OF_SERVICE("out:of:service")
    ;

    private final String RentalStatusType;
}

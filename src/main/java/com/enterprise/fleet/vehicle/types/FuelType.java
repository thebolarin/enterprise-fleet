package com.enterprise.fleet.vehicle.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum FuelType {
    PETROL("petrol"),
    DIESEL("diesel"),
    ELECTRICITY("electricity")
    ;

    private final String fuelType;
}

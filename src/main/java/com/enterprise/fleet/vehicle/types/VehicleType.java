package com.enterprise.fleet.vehicle.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum VehicleType {
    SUV("petrol"),
    COUPE("diesel"),
    MINIVAN("minivan"),
    PICKUP_TRUCK("pickup_truck"),
    CONVERTIBLE("convertible"),
    SEDAN("sedan"),
    HATCH_BACK("hatch_back"),
    ;

    private final String vehicleType;
}

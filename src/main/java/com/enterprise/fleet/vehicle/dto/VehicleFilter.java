package com.enterprise.fleet.vehicle.dto;

import com.enterprise.fleet.vehicle.types.FuelType;
import com.enterprise.fleet.vehicle.types.StatusType;
import com.enterprise.fleet.vehicle.types.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleFilter {
    private StatusType status;
    private VehicleType vehicleType;
    private String model;
    private String manufacturer;
    private String licensePlate;
    private Integer year;
    private FuelType fuelType;
}

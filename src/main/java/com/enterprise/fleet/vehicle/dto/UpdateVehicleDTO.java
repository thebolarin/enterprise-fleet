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
public class UpdateVehicleDTO {
    private String model;
    private String manufacturer;
    private Integer year;
    private FuelType fuelType;
    private String licensePlate;
    private StatusType status;
    private VehicleType type;
    private String locationId;
}

package com.enterprise.fleet.vehicle.dto;

import com.enterprise.fleet.vehicle.types.FuelType;
import com.enterprise.fleet.vehicle.types.StatusType;
import com.enterprise.fleet.vehicle.types.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleDTO {

    @NotBlank(message = "Vehicle model cannot be blank")
    private String model;

    @NotBlank(message = "Vehicle manufacturer cannot be blank")
    private String manufacturer;

    @NotNull(message = "Vehicle year cannot be blank")
    private Integer year;

    @NotNull(message = "Vehicle location cannot be blank")
    private Integer locationId;

    @NotNull(message = "Vehicle fuel type cannot be blank")
    private FuelType fuelType;

    @NotBlank(message = "Vehicle license plate cannot be blank")
    private String licensePlate;

    @NotNull(message = "Vehicle activity status cannot be blank")
    private StatusType status;

    @NotNull(message = "Type of Vehicle cannot be blank")
    private VehicleType type;
}

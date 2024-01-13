package com.enterprise.fleet.vehicle_rental.dto;

import com.enterprise.fleet.vehicle_rental.types.RentalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVehicleRentalDTO {
    private Integer vehicleId;
    private RentalStatusType rentalStatus;
    private LocalDate rentalDate;
    private LocalDate returnDate;
}

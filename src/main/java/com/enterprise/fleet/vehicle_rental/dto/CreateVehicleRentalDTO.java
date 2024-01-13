package com.enterprise.fleet.vehicle_rental.dto;

import com.enterprise.fleet.vehicle_rental.types.RentalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleRentalDTO {
    @NotBlank(message = "Vehicle cannot be blank")
    private Integer vehicleId;

    @NotBlank(message = "Customer cannot be blank")
    private Integer customerId;

    @NotNull(message = "Vehicle Rental status cannot be blank")
    private RentalStatusType rentalStatus;

    @NotBlank(message = "Vehicle Rental Date cannot be blank")
    private LocalDate rentalDate;

    @NotBlank(message = "Vehicle Return Date cannot be blank")
    private LocalDate returnDate;
}

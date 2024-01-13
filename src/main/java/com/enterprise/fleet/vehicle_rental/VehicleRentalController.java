package com.enterprise.fleet.vehicle_rental;

import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import com.enterprise.fleet.vehicle_rental.dto.CreateVehicleRentalDTO;
import com.enterprise.fleet.vehicle_rental.dto.UpdateVehicleRentalDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/vehicle-rental")
@RequiredArgsConstructor
public class VehicleRentalController {
    private final VehicleRentalService vehicleRentalService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> fetchVehicleRentals() {
        return vehicleRentalService.fetchVehicleRentals();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchVehicleRental(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return vehicleRentalService.fetchVehicleRental(id);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> customerCreateVehicleRental(
            @Valid @RequestBody CreateVehicleRentalDTO request,
            Principal connectedUser,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return vehicleRentalService.customerCreateVehicleRental(request, connectedUser);
    }

    @PostMapping("/sales-rep")
    public ResponseEntity<CustomResponse> createVehicleRentalForCustomer(
            @Valid @RequestBody CreateVehicleRentalDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return vehicleRentalService.createVehicleRentalForCustomer(request);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateVehicleRental(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,
            @Valid @RequestBody UpdateVehicleRentalDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return vehicleRentalService.updateVehicleRental(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> deleteVehicleRental(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return vehicleRentalService.deleteVehicleRental(id);
    }
}

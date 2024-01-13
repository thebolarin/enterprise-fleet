package com.enterprise.fleet.vehicle;

import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import com.enterprise.fleet.vehicle.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<CustomResponse> fetchVehicles() {
        return vehicleService.fetchVehicles();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchVehicle(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return vehicleService.fetchVehicle(id);
    }

    @GetMapping("/search")
    public ResponseEntity<CustomResponse> searchByFilter(
            @ModelAttribute VehicleFilter filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of( 1, size); // Page numbers start from 0

        Page<Vehicle> result = vehicleService.findByFilter(filter, pageable);

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicles listed successfully", result)
        );
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createVehicle (
            @Valid @RequestBody CreateVehicleDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return vehicleService.createVehicle(request);
    }

    @PutMapping(path = "/{vehicleId}/location/{locationId}")
    public ResponseEntity<CustomResponse> assignVehicleToLocation (
            @PathVariable(name = "vehicleId")
            @Min(value = 1, message = "Id must be greater than zero") int vehicleId,
            @PathVariable(name = "locationId")
            @Min(value = 1, message = "Id must be greater than zero") int locationId
    ) {
        return vehicleService.assignVehicleToLocation(locationId, vehicleId);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateVehicle(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,
            @Valid @RequestBody UpdateVehicleDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return vehicleService.updateVehicle(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> deleteVehicle(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return vehicleService.deleteVehicle(id);
    }
}

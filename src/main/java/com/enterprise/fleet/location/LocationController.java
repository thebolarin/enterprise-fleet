package com.enterprise.fleet.location;

import com.enterprise.fleet.location.dto.CreateLocationDTO;
import com.enterprise.fleet.location.dto.UpdateLocationDTO;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> fetchLocations() {
        return locationService.fetchLocations();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchLocation(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return locationService.fetchLocation(id);
    }

    @PostMapping("")
    public ResponseEntity<CustomResponse> createLocation(
            @Valid @RequestBody CreateLocationDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return locationService.createLocation(request);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateLocation(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,
            @Valid @RequestBody UpdateLocationDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return locationService.updateLocation(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> deleteLocation(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return locationService.deleteLocation(id);
    }
}

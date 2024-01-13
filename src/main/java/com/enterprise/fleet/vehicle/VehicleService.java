package com.enterprise.fleet.vehicle;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.location.Location;
import com.enterprise.fleet.location.LocationRepository;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.vehicle.dto.CreateVehicleDTO;
import com.enterprise.fleet.vehicle.dto.UpdateVehicleDTO;
import com.enterprise.fleet.vehicle.dto.VehicleFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final LocationRepository locationRepository;

    public ResponseEntity<CustomResponse> fetchVehicles() {
        var vehicles = vehicleRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicles listed successfully", vehicles)
        );
    }

    public Page<Vehicle> findByFilter(VehicleFilter filter, Pageable pageable) {
        return vehicleRepository.findByStatusAndTypeAndModelAndManufacturerAndLicensePlateAndYearAndFuelType(
                Optional.ofNullable(filter.getStatus()),
                Optional.ofNullable(filter.getVehicleType()),
                Optional.ofNullable(filter.getModel()),
                Optional.ofNullable(filter.getManufacturer()),
                Optional.ofNullable(filter.getLicensePlate()),
                Optional.ofNullable(filter.getYear()),
                Optional.ofNullable(filter.getFuelType()),
                pageable
        );
    }

    public ResponseEntity<CustomResponse> createVehicle(CreateVehicleDTO request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new CustomException("Vehicle Location not found", HttpStatus.NOT_FOUND));


        if (location.getCapacity() <= location.getVehicles().size()) {
            throw new CustomException("Location capacity reached. Cannot add more vehicles.");
        }

        var vehicle = Vehicle.builder()
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .year(request.getYear())
                .fuelType(request.getFuelType())
                .licensePlate(request.getLicensePlate())
                .status(request.getStatus())
                .type(request.getType())
                .location(location)
                .build();

        var savedVehicle = vehicleRepository.save(vehicle);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedVehicle.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body(
                        new CustomResponse(true, "Vehicle created successfully", savedVehicle)
                );
    }

    public ResponseEntity<CustomResponse> assignVehicleToLocation(int locationId, int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new CustomException("Vehicle not found", HttpStatus.NOT_FOUND));

        Location location = locationRepository.findById(locationId).orElseThrow(() ->
                new CustomException("Vehicle Location not found", HttpStatus.NOT_FOUND)
        );

        if (location.getCapacity() > location.getVehicles().size()) {
            location.getVehicles().add(vehicle);
            vehicle.setLocation(location);
            locationRepository.save(location);
        } else {
            throw new CustomException("Location capacity reached. Cannot add more vehicles.");
        }

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle has been assigned to a location", vehicle)
        );
    }

    public ResponseEntity<CustomResponse> fetchVehicle(int vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new CustomException("Vehicle not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle fetched successfully", vehicle)
        );
    }

    public ResponseEntity<CustomResponse> updateVehicle(int vehicleId, UpdateVehicleDTO request) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new CustomException("Vehicle not found", HttpStatus.NOT_FOUND));

        if (request.getType() != null) {
            vehicle.setType(request.getType());
        }

        if (request.getModel() != null) {
            vehicle.setModel(request.getModel());
        }

        if (request.getManufacturer() != null) {
            vehicle.setManufacturer(request.getManufacturer());
        }

        if (request.getYear() != null) {
            vehicle.setYear(request.getYear());
        }

        if (request.getFuelType() != null) {
            vehicle.setFuelType(request.getFuelType());
        }

        if (request.getLicensePlate() != null) {
            vehicle.setLicensePlate(request.getLicensePlate());
        }

        if (request.getStatus() != null) {
            vehicle.setStatus(request.getStatus());
        }

        vehicleRepository.save(vehicle);

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle updated successfully", vehicle)
        );
    }

    public ResponseEntity<CustomResponse> deleteVehicle(int vehicleId) {
        vehicleRepository.findById(vehicleId).orElseThrow(() ->
                new CustomException("Vehicle not found", HttpStatus.NOT_FOUND));

        vehicleRepository.deleteById(vehicleId);

        return ResponseEntity.ok().body(
                new CustomResponse(true, "Vehicle deleted successfully", null)
        );
    }
}

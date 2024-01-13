package com.enterprise.fleet.vehicle_rental;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.vehicle.Vehicle;
import com.enterprise.fleet.vehicle.VehicleRepository;
import com.enterprise.fleet.vehicle_rental.dto.CreateVehicleRentalDTO;
import com.enterprise.fleet.vehicle_rental.dto.UpdateVehicleRentalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VehicleRentalService {
    private final VehicleRentalRepository vehicleRentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public ResponseEntity<CustomResponse> fetchVehicleRentals(){
        var vehicles = vehicleRentalRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle Rentals listed successfully",vehicles)
        );
    }

    public ResponseEntity<CustomResponse> createVehicleRental(
            Integer customerId, Integer vehicleId, LocalDate rentalDate, LocalDate returnDate) {

        User user = userRepository.findById(customerId).orElseThrow(
                () -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new CustomException("Vehicle not found", HttpStatus.NOT_FOUND));

        var vehicleRental = VehicleRental.builder()
                .customer(user)
                .vehicle(vehicle)
                .returnDate(returnDate)
                .rentalDate(rentalDate)
                .build();

        var savedVehicleRental = vehicleRentalRepository.save(vehicleRental);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedVehicleRental.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body(
                        new CustomResponse(true, "Vehicle Rental created successfully", savedVehicleRental)
                );
    }

    public ResponseEntity<CustomResponse> customerCreateVehicleRental(
            CreateVehicleRentalDTO request, Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(request.getCustomerId() == null) throw new CustomException("Customer is required", HttpStatus.BAD_REQUEST);

        return createVehicleRental(
                user.getId(), request.getVehicleId(), request.getRentalDate(), request.getReturnDate());
    }

    public ResponseEntity<CustomResponse> createVehicleRentalForCustomer(
            CreateVehicleRentalDTO request) {

        return createVehicleRental(
                request.getCustomerId(), request.getVehicleId(), request.getRentalDate(), request.getReturnDate());
    }


    public ResponseEntity<CustomResponse> fetchVehicleRental(int vehicleRentalId){
        VehicleRental vehicleRental = vehicleRentalRepository.findById(vehicleRentalId).orElseThrow(
                () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle Rental fetched successfully", vehicleRental)
        );
    }

    public ResponseEntity<CustomResponse> updateVehicleRental(int vehicleRentalId, UpdateVehicleRentalDTO request){
        VehicleRental vehicleRental = vehicleRentalRepository.findById(vehicleRentalId).orElseThrow(
                () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND));

        if (request.getRentalStatus() != null) {
            vehicleRental.setRentalStatus(request.getRentalStatus());
        }

        if (request.getReturnDate() != null) {
            vehicleRental.setReturnDate(request.getReturnDate());
        }

        vehicleRentalRepository.save(vehicleRental);

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle Rental updated successfully", vehicleRental)
        );
    }

    public ResponseEntity<CustomResponse> deleteVehicleRental(int vehicleRentalId){
        vehicleRentalRepository.findById(vehicleRentalId).orElseThrow(
                () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND));

        vehicleRentalRepository.deleteById(vehicleRentalId);

        return ResponseEntity.ok().body(
                new CustomResponse(true, "Vehicle Rental deleted successfully", null)
        );
    }
}

package com.enterprise.fleet.location;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.location.dto.CreateLocationDTO;
import com.enterprise.fleet.location.dto.UpdateLocationDTO;
import com.enterprise.fleet.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public ResponseEntity<CustomResponse> fetchLocations(){
        var locations = locationRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Locations listed successfully",locations)
        );
    }

    public ResponseEntity<CustomResponse> createLocation(CreateLocationDTO request){
        var location = Location.builder()
                .city(request.getCity())
                .address(request.getAddress())
                .postcode(request.getPostcode())
                .capacity(request.getCapacity())
                .build();

        var savedLocation = locationRepository.save(location);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLocation.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body(
                        new CustomResponse(true, "Location created successfully", savedLocation)
                );
    }

    public ResponseEntity<CustomResponse> fetchLocation(int locationId){
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new CustomException("Location not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new CustomResponse(true, "Location fetched successfully", location)
        );
    }

    public ResponseEntity<CustomResponse> updateLocation(int locationId, UpdateLocationDTO request){
        Location location = locationRepository.findById(locationId).orElseThrow(
                () -> new CustomException("Location not found", HttpStatus.NOT_FOUND));

        if (request.getCity() != null) {
            location.setCity(request.getCity());
        }

        if (request.getAddress() != null) {
            location.setAddress(request.getAddress());
        }

        if (request.getPostcode() != null) {
            location.setPostcode(request.getPostcode());
        }

        if (request.getCapacity() != null) {
            location.setCapacity(request.getCapacity());
        }

        locationRepository.save(location);

        return ResponseEntity.ok(
                new CustomResponse(true, "Location updated successfully", location)
        );
    }

    public ResponseEntity<CustomResponse> deleteLocation(int locationId) {
            locationRepository.findById(locationId).orElseThrow(
                    () -> new CustomException("Location not found", HttpStatus.NOT_FOUND));

        locationRepository.deleteById(locationId);

            return ResponseEntity.ok().body(
                    new CustomResponse(true, "Location deleted successfully", null)
            );
    }
}



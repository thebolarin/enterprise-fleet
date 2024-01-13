package com.enterprise.fleet.vehicle;

import com.enterprise.fleet.vehicle.types.FuelType;
import com.enterprise.fleet.vehicle.types.StatusType;
import com.enterprise.fleet.vehicle.types.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>, JpaSpecificationExecutor<Vehicle> {
    Page<Vehicle> findByStatusAndTypeAndModelAndManufacturerAndLicensePlateAndYearAndFuelType(
            Optional<StatusType> status, Optional<VehicleType> type,
            Optional<String> model, Optional<String> manufacturer,
            Optional<String> licensePlate, Optional<Integer> year,
            Optional<FuelType> fuelType, Pageable pageable
    );
}


package com.enterprise.fleet.location;

import com.enterprise.fleet.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
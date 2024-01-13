package com.enterprise.fleet.vehicle;

import com.enterprise.fleet.location.Location;
import com.enterprise.fleet.vehicle.types.*;
import com.enterprise.fleet.vehicle_rental.VehicleRental;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicles_id_seq")
    @SequenceGenerator(name = "vehicles_id_seq", sequenceName = "vehicles_id_seq", allocationSize = 1)
    private Integer id;
    private String model;
    private String manufacturer;
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    @Column(name = "license_plate")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType type;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.EAGER)
    private List<VehicleRental> vehicleRentals;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    public Location location;
}
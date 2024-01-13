package com.enterprise.fleet.user.entity;

import com.enterprise.fleet.vehicle_rental.VehicleRental;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User {
    private String address;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    private User salesRep;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<VehicleRental> vehicleRentals;
}

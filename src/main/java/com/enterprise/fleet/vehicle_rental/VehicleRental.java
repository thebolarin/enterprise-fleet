package com.enterprise.fleet.vehicle_rental;

import com.enterprise.fleet.invoice.Invoice;
import com.enterprise.fleet.transaction.Transaction;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.vehicle.Vehicle;
import com.enterprise.fleet.vehicle_rental.types.RentalStatusType;
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
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicle_rentals")
public class VehicleRental {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_rentals_id_seq")
    @SequenceGenerator(name = "vehicle_rentals_id_seq", sequenceName = "vehicle_rentals_id_seq", allocationSize = 1)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_status")
    private RentalStatusType rentalStatus;

    @Column(name = "rental_date")
    private LocalDate rentalDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User customer;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @JsonManagedReference
    @OneToOne(mappedBy = "vehicleRental")
    private Invoice invoice;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicleRental", fetch = FetchType.LAZY)
    private List<Transaction> transaction;
}


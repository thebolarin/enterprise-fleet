package com.enterprise.fleet.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SalesRep extends User {
    @JsonManagedReference
    @OneToMany(mappedBy = "salesRep")
    private List<Customer> customers;
}

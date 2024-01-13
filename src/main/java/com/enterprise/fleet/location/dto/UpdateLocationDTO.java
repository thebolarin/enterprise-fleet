package com.enterprise.fleet.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLocationDTO {
    private String city;
    private String address;
    private String postcode;
    private Integer capacity;
}
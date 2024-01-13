package com.enterprise.fleet.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocationDTO {
    @NotBlank(message = "Location postcode year cannot be blank")
    private String postcode;

    @NotBlank(message = "Location name cannot be blank")
    private String city;

    @NotBlank(message = "Location address cannot be blank")
    private String address;

    @NotNull(message = "Location capacity cannot be blank")
    private Integer capacity;
}

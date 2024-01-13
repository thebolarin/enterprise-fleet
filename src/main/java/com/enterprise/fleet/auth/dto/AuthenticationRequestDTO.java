package com.enterprise.fleet.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {
  @Email(message = "Email is not in the correct format")
  private String email;
  @NotBlank(message = "Password cannot be blank")
  String password;
}

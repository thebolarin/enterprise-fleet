package com.enterprise.fleet.auth.dto;

import com.enterprise.fleet.user.types.Role;
import jakarta.validation.constraints.Email;
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
public class RegisterRequestDTO {
  @NotBlank(message = "First name cannot be blank")
  private String firstName;
  @NotBlank(message = "Last name cannot be blank")
  private String lastName;
  @Email(message = "Email is not in the correct format")
  private String email;
  @NotBlank(message = "Password cannot be blank")
  private String password;
  @NotNull(message = "Role cannot be blank")
  private Role role;
}

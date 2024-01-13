package com.enterprise.fleet.auth;

import com.enterprise.fleet.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
  private String accessToken;
  private String refreshToken;
  private User userDetails;
}

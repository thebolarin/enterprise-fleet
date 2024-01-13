package com.enterprise.fleet.auth;

import com.enterprise.fleet.auth.dto.AuthenticationRequestDTO;
import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<CustomResponse> register(
          @Valid  @RequestBody RegisterRequestDTO request,
          BindingResult bindingResult
  ) {

    if (bindingResult.hasErrors()) {
      ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
      return validationResponseHandler.handleValidationErrors();
    }

    User newUser = service.register(request);

    String uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newUser.getId())
            .toUriString();

    return ResponseEntity.created(java.net.URI.create(uri))
            .body( new CustomResponse(true, "User Registered successfully", newUser) );
  }

  @PostMapping("/login")
  public ResponseEntity<CustomResponse> authenticate(
          @RequestBody AuthenticationRequestDTO request
  ) {
    AuthenticationResponse result = service.authenticate(request);

    if(result == null){
      throw new CustomException("Unable to authenticate customer");
    }

    return ResponseEntity.ok(
            new CustomResponse(false, "Customer authenticated successfully", result)
    );
  }

  @PostMapping("/admin/login")
  public ResponseEntity<CustomResponse> authenticateAdmin(
      @RequestBody AuthenticationRequestDTO request
  ) {
    AuthenticationResponse result = service.authenticateAdmin(request);

    if(result == null){
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
              new CustomResponse(false, "Unable to authenticate user due to incorrect email/password", result)
      );
    }

    return ResponseEntity.ok(
            new CustomResponse(true, "User authenticated successfully", result)
    );
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<CustomResponse> refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
    return ResponseEntity.ok(
            new CustomResponse(true, "Token has been refreshed", null)
    );
  }
}

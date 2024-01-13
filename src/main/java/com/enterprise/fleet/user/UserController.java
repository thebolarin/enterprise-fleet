package com.enterprise.fleet.user;

import com.enterprise.fleet.user.dto.CreateCustomerRequestDTO;
import com.enterprise.fleet.user.dto.UpdateUserDTO;
import com.enterprise.fleet.user.entity.Customer;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.exception.ValidationResponseHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<CustomResponse> fetchUsers() {
        return userService.fetchUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> fetchUser(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return userService.fetchUser(id);
    }

    @PutMapping ("/password")
    public ResponseEntity<?> changePassword(
          @RequestBody ChangePasswordRequest request,
          Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomResponse> createCustomer(
            @RequestBody CreateCustomerRequestDTO request,
            Principal connectedUser
    ) {
        Customer newCustomer = userService.createCustomer(request, connectedUser);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCustomer.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body( new CustomResponse(true, "Customer created successfully", newCustomer) );
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> updateUser (
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id,

            @Valid @RequestBody UpdateUserDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            ValidationResponseHandler validationResponseHandler = new ValidationResponseHandler(bindingResult);
            return validationResponseHandler.handleValidationErrors();
        }

        return userService.updateUser(id, request);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CustomResponse> userTransaction(
            @PathVariable(name = "id")
            @Min(value = 1, message = "Id must be greater than zero") int id
    ) {
        return userService.deleteUser(id);
    }
}

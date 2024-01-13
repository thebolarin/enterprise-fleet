package com.enterprise.fleet.user;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.user.dto.CreateCustomerRequestDTO;
import com.enterprise.fleet.user.dto.UpdateUserDTO;
import com.enterprise.fleet.user.entity.Customer;
import com.enterprise.fleet.user.entity.SalesRep;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.CustomerRepository;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.user.types.Role;
import com.enterprise.fleet.user.types.UserStatusType;
import com.enterprise.fleet.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public ResponseEntity<CustomResponse> fetchUsers(){
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Users listed successfully", users)
        );
    }

    public ResponseEntity<CustomResponse> fetchUser(int userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new CustomResponse(true, "User fetched successfully", user)
        );
    }
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException("Incorrect password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new CustomException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    public Customer createCustomer(CreateCustomerRequestDTO request, Principal connectedUser) {
        SalesRep salesRep = (SalesRep) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Customer with this email already exists", HttpStatus.CONFLICT);
        }

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setStatus(UserStatusType.ACTIVE);
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customer.setSalesRep(salesRep);
        return customerRepository.save(customer);
    }

    public ResponseEntity<CustomResponse> updateUser(int userId, UpdateUserDTO request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        userRepository.save(user);

        return ResponseEntity.ok(
                new CustomResponse(true, "User updated successfully", user)
        );
    }

    public ResponseEntity<CustomResponse> deleteUser(int userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        userRepository.deleteById(userId);

        return ResponseEntity.ok().body(
                new CustomResponse(true, "User deleted successfully", null)
        );
    }
}

package com.enterprise.fleet.auth;

import com.enterprise.fleet.auth.dto.AuthenticationRequestDTO;
import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.config.JwtService;
import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.token.Token;
import com.enterprise.fleet.token.TokenRepository;
import com.enterprise.fleet.token.TokenType;
import com.enterprise.fleet.user.types.Role;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.user.types.UserStatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequestDTO request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new CustomException("User with this email already exists", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .status(UserStatusType.ACTIVE)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        return repository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException("Customer not found", HttpStatus.NOT_FOUND));

            if (user.getRole() != Role.CUSTOMER) {
                throw new CustomException("Customer access denied", HttpStatus.FORBIDDEN);
            };

            if (user.getStatus() != UserStatusType.ACTIVE) {
                throw new CustomException("Customer access denied", HttpStatus.FORBIDDEN);
            };

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .userDetails(user)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException ex) {
            // Log or handle the authentication failure
            System.out.println("Authentication failed: " + ex);
            return null;
        }
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            if (user.getRole() == Role.CUSTOMER) return null;

            if (user.getStatus() != UserStatusType.ACTIVE) {
                throw new CustomException("User access denied", HttpStatus.FORBIDDEN);
            };

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .userDetails(user)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException ex) {
            // Log or handle the authentication failure
            System.out.println("Authentication failed: " + ex);
            return null;
        }
    }

    public AuthenticationResponse login(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            if (user.getRole() == Role.CUSTOMER) return null;

            if (user.getStatus() != UserStatusType.ACTIVE) {
                throw new CustomException("User access denied", HttpStatus.FORBIDDEN);
            };

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .userDetails(user)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (AuthenticationException ex) {
            // Log or handle the authentication failure
            System.out.println("Authentication failed: " + ex);
            return null;
        }
    }
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}

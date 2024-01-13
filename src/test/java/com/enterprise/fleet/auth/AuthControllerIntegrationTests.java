package com.enterprise.fleet.auth;

import com.enterprise.fleet.auth.dto.AuthenticationRequestDTO;
import com.enterprise.fleet.auth.dto.RegisterRequestDTO;
import com.enterprise.fleet.user.types.Role;
import com.enterprise.fleet.user.entity.User;
import com.enterprise.fleet.user.repository.UserRepository;
import com.enterprise.fleet.user.types.UserStatusType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerIntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void givenExistingEmail_whenRegisteringUser_thenConflictResponse() throws Exception {
        String password = faker.internet().password();
        User user = buildUser(password);
        RegisterRequestDTO registerRequestDTO = createRegisterRequestDTO(user, password);

        userRepository.save(user);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }

    @Test
    public void givenNewUser_whenRegisteringUser_thenSuccessfulRegistration() throws Exception {
        String password = faker.internet().password();
        User user = buildUser(password);
        RegisterRequestDTO registerRequestDTO = createRegisterRequestDTO(user, password);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("User Registered successfully"));
    }

    @Test
    public void givenRegisteredUser_whenSigningIn_thenSuccessfulAuthentication() throws Exception {
        String password = faker.internet().password();

        User user = buildUser(password);

        RegisterRequestDTO registerRequestDTO = createRegisterRequestDTO(user, password);
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("User Registered successfully"));

        AuthenticationRequestDTO authenticationRequestDTO = new AuthenticationRequestDTO();
        authenticationRequestDTO.setEmail(user.getEmail());
        authenticationRequestDTO.setPassword(password);

        mockMvc.perform(post("/api/v1/auth/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("User authenticated successfully"));
    }

    private User buildUser(String password) {
        return User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .status(UserStatusType.ACTIVE)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();
    }

    private RegisterRequestDTO createRegisterRequestDTO(User user, String password) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setFirstName(user.getFirstName());
        registerRequestDTO.setLastName(user.getLastName());
        registerRequestDTO.setEmail(user.getEmail());
        registerRequestDTO.setPassword(password);
        registerRequestDTO.setRole(user.getRole());
        return registerRequestDTO;
    }

//    private void performRegistrationRequest(RegisterRequestDTO registerRequestDTO) throws Exception {
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.status").value(true))
//                .andExpect(jsonPath("$.message").value("User Registered successfully"));
//    }
}
